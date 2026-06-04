import {
  AngularNodeAppEngine,
  createNodeRequestHandler,
  isMainModule,
  writeResponseToNodeResponse,
} from '@angular/ssr/node';
import express from 'express';
import { join } from 'node:path';
import { readFileSync, existsSync } from 'node:fs';

const browserDistFolder = join(import.meta.dirname, '../browser');

const app = express();
const angularApp = new AngularNodeAppEngine();

// Lee el .env en tiempo de ejecucion para evitar cualquier cache de archivo
function loadEnv(): Record<string, string> {
  const envPath = join(import.meta.dirname, '../../../.env');
  if (!existsSync(envPath)) return {};
  const result: Record<string, string> = {};
  for (const line of readFileSync(envPath, 'utf-8').split('\n')) {
    const trimmed = line.trim();
    if (!trimmed || trimmed.startsWith('#')) continue;
    const idx = trimmed.indexOf('=');
    if (idx === -1) continue;
    result[trimmed.slice(0, idx).trim()] = trimmed.slice(idx + 1).trim();
  }
  return result;
}

const ALLOWED_KEYS = [
  'BACKEND_API', 'API_USERS', 'API_APPOINTMENTS', 'API_DOCTOR',
  'API_REPORTS', 'API_AUTH', 'KEYCLOAK_TOKEN_URL', 'KEYCLOAK_CLIENT_ID', 'KEYCLOAK_CLIENT_SECRET'
];

/**
 * env.js generado dinamicamente desde .env en cada request — nunca se cachea
 */
app.get('/assets/env.js', (_req, res) => {
  const fileEnv = loadEnv();
  const env = Object.fromEntries(
    ALLOWED_KEYS.filter(k => fileEnv[k]).map(k => [k, fileEnv[k]])
  );
  res.set('Cache-Control', 'no-cache, no-store, must-revalidate');
  res.set('Content-Type', 'application/javascript');
  res.send(`window.__env = ${JSON.stringify(env, null, 2)};\n`);
});

/**
 * Serve static files from /browser
 */
app.use(
  express.static(browserDistFolder, {
    maxAge: '1y',
    index: false,
    redirect: false,
  }),
);

/**
 * Handle all other requests by rendering the Angular application.
 */
app.use((req, res, next) => {
  angularApp
    .handle(req)
    .then((response) =>
      response ? writeResponseToNodeResponse(response, res) : next(),
    )
    .catch(next);
});

if (isMainModule(import.meta.url) || process.env['pm_id']) {
  const port = process.env['PORT'] || 4000;
  app.listen(port, (error) => {
    if (error) throw error;
    console.log(`Node Express server listening on http://localhost:${port}`);
  });
}

export const reqHandler = createNodeRequestHandler(app);

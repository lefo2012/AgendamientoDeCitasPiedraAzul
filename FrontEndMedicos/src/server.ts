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

app.use(
  express.static(browserDistFolder, {
    maxAge: '1y',
    index: false,
    redirect: false,
  }),
);

app.use(async (req, res, next) => {
  try {
    const response = await angularApp.handle(req);
    if (!response) return next();

    // Inyectar window.__env inline en el HTML — elimina cualquier problema
    // de timing o cache con el archivo env.js externo
    const html = await response.text();
    const fileEnv = loadEnv();
    const env = Object.fromEntries(
      ALLOWED_KEYS.filter(k => fileEnv[k]).map(k => [k, fileEnv[k]])
    );
    const inlineScript = `<script>window.__env=${JSON.stringify(env)};</script>`;
    const modified = html.replace(
      '<script src="assets/env.js"></script>',
      inlineScript
    );

    response.headers.forEach((value, key) => {
      if (key.toLowerCase() !== 'content-length') res.setHeader(key, value);
    });
    res.status(response.status).send(modified);
  } catch (err) {
    next(err);
  }
});

if (isMainModule(import.meta.url) || process.env['pm_id']) {
  const port = process.env['PORT'] || 4000;
  app.listen(port, (error) => {
    if (error) throw error;
    console.log(`Node Express server listening on http://localhost:${port}`);
  });
}

export const reqHandler = createNodeRequestHandler(app);

const fs = require('fs');
const path = require('path');
const dotenv = require('dotenv');

const rootDir = path.resolve(__dirname, '..');
const envPath = path.join(rootDir, '.env');
const outputPath = path.join(rootDir, 'src', 'assets', 'env.js');

const envResult = dotenv.config({ path: envPath });
const env = envResult.parsed || {};

const allowedKeys = [
  'BACKEND_API',
  'API_USERS',
  'API_APPOINTMENTS',
  'API_DOCTOR',
  'API_REPORTS',
  'API_AUTH',
  'KEYCLOAK_TOKEN_URL',
  'KEYCLOAK_CLIENT_ID',
  'KEYCLOAK_CLIENT_SECRET'
];

const selectedEnv = allowedKeys.reduce((acc, key) => {
  if (Object.prototype.hasOwnProperty.call(env, key)) {
    acc[key] = env[key];
  }
  return acc;
}, {});

const output = `window.__env = ${JSON.stringify(selectedEnv, null, 2)};\n`;

fs.mkdirSync(path.dirname(outputPath), { recursive: true });
fs.writeFileSync(outputPath, output, 'utf8');
console.log(`[env] Wrote ${outputPath}`);

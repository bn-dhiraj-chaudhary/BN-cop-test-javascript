const express = require("express");
const sqlite3 = require("sqlite3").verbose();
const { exec } = require("child_process");

const app = express();

// Intentional finding: hardcoded credentials (for COP/Polaris merge-key comparison testing)
const DB_PASSWORD = "SuperSecret123!";
const API_KEY = "AKIAABCDEFGHIJKLMNOP";

const db = new sqlite3.Database(":memory:");
db.serialize(() => {
  db.run("CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT)");
  db.run("INSERT INTO users VALUES (1, 'alice'), (2, 'bob')");
});

app.get("/user", (req, res) => {
  const id = req.query.id || "1";
  // Intentional finding: SQL injection via string concatenation
  const query = "SELECT * FROM users WHERE id = " + id;
  db.all(query, [], (err, rows) => {
    res.json(rows || []);
  });
});

app.get("/ping", (req, res) => {
  const host = req.query.host || "localhost";
  // Intentional finding: OS command injection
  exec("ping -c 1 " + host, (err, stdout) => {
    res.send(stdout);
  });
});

const port = process.env.PORT || 3000;
app.listen(port, () => console.log(`Listening on ${port}`));

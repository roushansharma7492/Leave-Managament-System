# Employee Leave Management System

This repository contains a Java Spring Boot backend and a frontend application (if present).

## Backend (Java / Maven)

Requirements:
- JDK 25 (latest LTS as targeted by this project)
- Apache Maven (recommended) or use GitHub Actions CI

Local build steps:

```powershell
# from repo root
Set-Location 'c:\Users\PC\Desktop\Managemant system\employee-leave-system\backend'
# confirm Java
java -version
# build (requires mvn)
mvn -B -U clean verify
```

If you don't have Maven locally, install it or use the GitHub Actions CI on push/PR which uses Java 25 and Maven.

### If your local Java is older than 25
- Either install JDK 25 and point `JAVA_HOME` to it, or change the `pom.xml` Java target to your installed JDK version (in `properties`, update `java.version` and `maven.compiler.release`).

## Frontend

If a `frontend/package.json` exists, CI will attempt to run `npm ci` and `npm run build`. If your frontend is missing, add your frontend sources and `package.json` at `frontend/`.

## CI

A GitHub Actions workflow is included at `.github/workflows/ci.yml` — it builds the backend using Java 25 and will build the frontend if `frontend/package.json` is present.

## Next steps I can help with
- Generate a Maven Wrapper (`mvnw`) so contributors without Maven can build locally (requires running Maven once to generate wrapper files, or I can add pre-built wrapper files if you want).
- Create a branch and commit these changes and open a PR.
- Add Docker-based build/run scripts.

Tell me which you'd like me to do next.
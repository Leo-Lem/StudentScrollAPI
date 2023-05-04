# StudentScroll API

This is the REST API behind the StudentScroll app ([OpenAPI specs](https://studentscroll.net/api/v1/docs/swagger)).

[![CI/CD](https://github.com/Leo-Lem/StudentScrollAPI/actions/workflows/cicd.yml/badge.svg)](https://github.com/Leo-Lem/StudentScrollAPI/actions/workflows/cicd.yml)

## Setting up Maven for macOS

_Necessary for compiling, running, and everything in between_

1. (only macOS) Install using homebrew (`brew install maven`)
2. Now the maven commands should work (e.g., `mvn compile`, `mvn package`, [Docs](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html))

## Setting up Maven for Win11

1. Find and download wanted version from website ([Apache Maven Project](https://maven.apache.org/download.cgi))
2. Follow this online instruction to configure system variables ([Install java and maven in windows 11](https://community.jaspersoft.com/blog/install-java-and-maven-windows-11))
3. Open CLI and try ('mvn -version') the MAVEN information shall display, and now the maven commands should work.

## API endpoints

OpenAPI specs are now automatically created when executing. They can be accessed at

- /docs - for JSON.
- /docs.yaml - for YAML.
- /docs/swagger - for interactive Swagger UI.

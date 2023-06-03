# StudentScroll API

[![CI/CD](https://github.com/Leo-Lem/StudentScrollAPI/actions/workflows/cicd.yml/badge.svg)](https://github.com/Leo-Lem/StudentScrollAPI/actions/workflows/cicd.yml)

This is the REST API behind the StudentScroll app ([OpenAPI specs](https://studentscroll.net/api/v1/docs/swagger)).

Here we provide our [StudentScroll web app](https://github.com/leo-lem/studentscrollapp) with its functionality.

[/api/v2](https://studentscroll.net/api/v2/docs/swagger-ui/index.html) The REST API (v2) is designed to be as simple and intuitive as possible, whilst providing all required functionality:

- [/account](https://studentscroll.net/api/v2/docs/swagger-ui/index.html#/account) This endpoint describes our account feature. Here we handle everything from sign up, through sign in, to updating credentials (forgotten password, etc.) and deleting your account.

  - [/settings](https://studentscroll.net/api/v2/docs/swagger-ui/index.html#/settings) Furthermore, you can individualise your account by updating your settings.

- [/students](https://studentscroll.net/api/v2/docs/swagger-ui/index.html#/students) This major endpoint describes our profile feature. Here you can update your individual profile, search for other student's profiles.

  - [/followers](https://studentscroll.net/api/v2/docs/swagger-ui/index.html#/students) We have also embedded the following mechanic beneath this endpoint. Here you can follow and unfollow other students.

- [/posts](https://studentscroll.net/api/v2/docs/swagger-ui/index.html#/posts) On the posts endpoint, you can, well, post what's on your mind. ALso, you can search for posts and update, or delete, your posts.

- [/chats](https://studentscroll.net/api/v2/docs/swagger-ui/index.html#/chats) Our chats feature enables you to create, find, and delete chats.

  - [/messages](https://studentscroll.net/api/v2/docs/swagger-ui/index.html#/messages) The embedded messages endpoint contains the messages in a given chat. Here you can send messages, update or delete them, as well as find a given message.

- [/maps](https://studentscroll.net/api/v2/docs/swagger-ui/index.html#/maps) This minor endpoint retrieves the Google Maps API key from our backend.

# Building, testing, and running the StudentScrollAPI

The StudentScrollAPI uses Maven as dependency manager and build tool.

Since the beginning, we have updated the way we run things. We now include a Maven wrapper. With this you can trigger any of the [Maven lifecycle phases](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html) by running the following from the repository root:

```bash
> % ./mvnw [compile | test | package | install | ...] # Unix
# or
> % ./mvnw.cmd [compile | test | package | install | ...] # Windows
```

Additionally, Spring Boot provides a custom run command for trying the project out on your local machine:

```bash
> % ./mvnw spring-boot:run # Unix
# or
> % ./mvnw.cmd spring-boot:run # Windows
```

# EasyShop

EasyShop is your no-frills, get-stuff-done e-commerce playground built with Spring Boot and MySQL. Whether you're adding
to your cart like it's Black Friday or tweaking your admin panel like a power user, this app has your back.

It serves a responsive single-page frontend straight from the `static` folder and delivers a full-stack experience
through a set of REST APIs secured with JWT authentication. In short: it's your starter pack for building online shops
the grown-up way.

## Features

- Sign up and log in with JWT-secured sessions.
- Customize your profile, including uploading a profile picture.
- Admin panel with product and category CRUD through stylish offcanvas UIs.
- Live product filtering and search, so you find what you want, fast.
- A simple but functional shopping cart with edit and remove options.
- Seamless checkout experience that turns your cart into an order.
- Sample product catalog thanks to preloaded MySQL data.

## Project Structure

Here’s a quick peek at what lives where:

```text
docker-composer.yml -> Local MySQL database container setup
database/create_database.sql -> Schema and dummy data
src/main/java -> Your Spring Boot Java code
src/main/resources/static -> HTML/CSS/JS for the frontend
```

## Running Locally

1. Spin up the MySQL container:

   ```bash
   docker compose -f docker-composer.yml up -d
   ```
2. Start the Spring Boot backend:

   ```bash
    mvn spring-boot:run
    ```
3. Visit the App:

    ```yaml
   http://localhost:8080
    ```


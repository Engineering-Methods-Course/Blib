# About BLib

## Background

BLib is an educational full-stack project built for an Engineering Methods course. The goal is to design and implement a complete client-server library management system, applying software engineering practices (use-case driven design, layered architecture, client-server protocols) to a realistic domain: managing a library's books, subscribers, and loans.

## Goals

- Practice designing a layered client-server system: presentation (JavaFX GUI) → logic → data (MySQL via JDBC).
- Apply a reusable client-server communication framework (OCSF) rather than hand-rolling networking.
- Model a real business process end-to-end: subscriber registration, catalog search, borrowing/returning, reservations, notifications, and librarian reporting.

## Scope

- **Client**: JavaFX desktop app for subscribers and librarians (search, borrow/return, profile management, reports, messaging).
- **Server**: handles client connections over OCSF, executes business logic, persists to MySQL, and sends email notifications.
- **OCSF**: the underlying client-server messaging framework shared by both client and server.

## Status

Completed as a final project for the Engineering Methods course (see `Diagrams/UseCase.png` for the modeled use cases). Not intended for production deployment as-is (no auth hardening, connection pooling, or production DB config included).

## Team

Maintained by the Engineering-Methods-Course group.

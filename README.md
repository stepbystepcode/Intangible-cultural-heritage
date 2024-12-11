# Intangible Cultural Heritage Information System

![License: GNU General Public License v3.0](https://img.shields.io/badge/License-GNU%20General%20Public%20License%20v3.0-blue.svg)

## 📖 Project Overview

This project is a collaborative database course design focused on creating an **Intangible Cultural Heritage (ICH) Information System**. It enables users to query, explore, and visualize various aspects of ICH data, including the cultural, geographic, and descriptive elements of different heritage entries. We have also integrated **map-based visualization** to help users locate heritage sites geographically.

## 🚀 Features

- **Comprehensive Query System**: Easily search and filter ICH entries by categories, regions, historical significance, etc.
- **Map Visualization**: Interactive map with **location-based visualization** for a better user experience, powered by Leaflet and React.
- **Detailed Heritage Information**: Each heritage entry includes descriptions, images, and other relevant metadata.
- **Responsive Design**: Works seamlessly across different devices and screen sizes.

## 🛠️ Tech Stack

### Frontend

- **[Bun](https://bun.sh/)**: High-performance JavaScript runtime
- **[RsBuild](https://rsbuild.dev/index)**: The Rspack Powered Build Tool
- **[React](https://reactjs.org/)**: UI library for building interactive user interfaces
- **[Tailwind CSS](https://tailwindcss.com/)**: Utility-first CSS framework for styling
- **[shadcn/ui](https://ui.shadcn.dev/)**: Component library for consistent UI components
- **[Leaflet React](https://react-leaflet.js.org/)**: Map library for displaying geolocation data

### Backend

- **[Kotlin](https://kotlinlang.org/)**: Programming language for concise and safe backend development
- **[Spring Boot](https://spring.io/projects/spring-boot)**: Framework for building microservices with Spring
- **[PostgreSQL](https://www.postgresql.org/)**: Robust relational database for data storage

### Other Tools

- <a href="https://draw.io" target="_blank" rel="noopener">**Draw.io**</a>: ER design tool used to create the database schema
- **[Git](https://git-scm.com/)** and **[GitHub](https://github.com/)**: Version control and project hosting
- **[WebStorm](https://www.jetbrains.com/webstorm/)** & **[IntelliJ IDEA](https://www.jetbrains.com/idea/)**: IDEs for frontend and backend development
- **[Postman](https://www.postman.com/)**: API testing and development
- **[Google Chrome](https://www.google.com/chrome/)**: Primary browser for frontend testing

## 📜 License

This project is licensed under the **GNU General Public License v3.0**. See the [LICENSE](./LICENSE) file for details.

## 📐 ER Diagram

The Entity-Relationship Diagram (ERD) for this project was designed using **DrawDB**. It illustrates the relationships between different entities in our ICH information system.

*ER Diagram preview image here (optional)*

## 🔧 Getting Started

Follow these steps to set up the project locally.

### Prerequisites

- **Bun**
- **Java** (JDK 21)
- **PostgreSQL** database setup

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/stepbystepcode/Intangible-cultural-heritage.git
   cd Intangible-cultural-heritage
   ```

2. **Install Frontend dependencies**:
   ```bash
   cd frontend
   bun i
   ```

3. **Install Backend dependencies**:
   ```bash
   cd ../backend
   ./gradlew build
   ```

### Database Setup

1. **Create a PostgreSQL database** for the ICH system.
2. **Run migration scripts** if available or use the ERD to set up tables manually.

### Running the Application

1. **Start the Backend**:
   ```bash
   cd backend
   ./gradlew bootRun
   ```

2. **Start the Frontend**:
   ```bash
   cd frontend
   bun dev
   ```

The application will be available at `http://localhost:5713` (or as specified).

## 📍 Map Visualization

Our system utilizes [**React-Leaflet**](https://react-leaflet.js.org) to provide a seamless map-based user experience. Users can view and interact with the locations of cultural heritage items directly on the map.

## 💡 Future Improvements

- **User Authentication**: Enable personalized experiences and save user preferences.
- **Advanced Filtering**: Enhance search functionality with additional filters.
- **Interactive Map Layers**: Add more detailed map layers for richer geographic context.

## 🤝 Contributors & Acknowledgments

This project is developed by a team of dedicated students. Special thanks to our team members for their hard work and collaboration:

- **[Jing Li](https://github.com/stepbystepcode)**
- **[Xinbo Zhang]()**
- **[Ziyang Zhang]()**
- **[Haoran Yu]()**
- **[Zhen Zhang]()**

---

*Built with passion for preserving and sharing intangible cultural heritage.* 🌏

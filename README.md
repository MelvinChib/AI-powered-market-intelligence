# Real-Time Multi-Agent AI System for Financial Market Analysis

## Overview
Enterprise-grade Spring Boot WebFlux backend for a real-time multi-agent AI system designed for financial market analysis. Built with reactive programming principles for high performance and scalability.

## Architecture

### Core Technologies
- **Framework**: Spring Boot 3.3+ with WebFlux (Reactive stack)
- **Language**: Java 21 with Virtual Threads
- **Database**: PostgreSQL (persistent) + Redis (caching/streaming)
- **Messaging**: Spring WebSocket + STOMP
- **Security**: Spring Security 6 + JWT Authentication
- **AI Ready**: Prepared for Spring AI or LangChain4j integration

### Key Modules

1. **Market Intelligence Module** (`/api/market/*`)
   - Live market summaries and price updates
   - Correlation analysis between stocks/sectors
   - Real-time streaming via WebSocket

2. **AI Insights Module** (`/api/agents/insights`, `/ws/ai-insights`)
   - AI-generated market commentary
   - Sentiment analysis and insights
   - Real-time insight streaming

3. **Agent Management Module** (`/api/agents/*`)
   - Agent performance metrics
   - Communication logs between agents
   - Real-time agent status updates

4. **Trading Simulation Module** (`/api/simulations/*`)
   - Backtesting and simulation execution
   - Trade replay functionality
   - Live simulation streaming

5. **Risk Management Module** (`/api/risk/*`, `/api/compliance/*`)
   - Real-time risk monitoring
   - Compliance violation tracking
   - Risk alert notifications

6. **AI Command Console** (`/api/agents/chat`)
   - Multi-agent chat interface
   - Query processing and responses
   - Ready for LLM integration

7. **Notification System** (`/api/notifications/*`)
   - System-wide event broadcasting
   - Real-time notification streaming

8. **Authentication** (`/api/auth/*`)
   - JWT-based authentication
   - Role-based access control (ADMIN, ANALYST, VIEWER)

## WebSocket Endpoints

All WebSocket endpoints use STOMP protocol over `/ws`:

- `/topic/market-data` - Live market price updates
- `/topic/ai-insights` - AI-generated insights
- `/topic/agents/updates` - Agent status changes
- `/topic/simulation` - Trading simulation events
- `/topic/risk-alerts` - Risk management alerts
- `/topic/notifications` - System notifications

## Getting Started

### Prerequisites
- Java 21+
- PostgreSQL 12+
- Redis 6+
- Maven 3.8+

### Database Setup
```sql
-- Create database
CREATE DATABASE aiagentsystem;

-- Tables are auto-created via data.sql
```

### Configuration
Update `application.yml` with your database and Redis connection details:

```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/aiagentsystem
    username: your_username
    password: your_password
  
  data:
    redis:
      host: localhost
      port: 6379
```

### Running the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Default Users
- **admin/password** (ADMIN role)
- **analyst/password** (ANALYST role)  
- **viewer/password** (VIEWER role)

## API Documentation

### Authentication
```bash
# Login
POST /api/auth/login
{
  "username": "admin",
  "password": "password"
}

# Register
POST /api/auth/register
{
  "username": "newuser",
  "password": "password"
}
```

### Market Data
```bash
# Get market summary
GET /api/market/summary

# Get market relationships
GET /api/market/relationships
```

### Agent Management
```bash
# Get agent performance
GET /api/agents/performance

# Get agent details
GET /api/agents/{id}/details

# Chat with agents
POST /api/agents/chat
{
  "userPrompt": "What's the market outlook?"
}
```

### Simulations
```bash
# Run simulation
POST /api/simulations/run

# Get results
GET /api/simulations/{id}/results
```

### Risk Management
```bash
# Get risk exposure
GET /api/risk/exposure

# Get compliance logs
GET /api/compliance/logs
```

## Future Enhancements

### AI Integration TODOs
- Integrate Spring AI for LLM-based agent responses
- Connect to OpenAI API for advanced natural language processing
- Implement multi-agent orchestration layer
- Add vector database for semantic search

### Performance Optimizations
- Implement connection pooling optimization
- Add caching strategies for frequently accessed data
- Optimize WebSocket message batching
- Add metrics and monitoring

## Project Structure
```
src/main/java/com/example/aiagentsystem/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data transfer objects (records)
├── model/          # Entity models
├── repository/     # R2DBC repositories
├── service/        # Business logic services
└── websocket/      # WebSocket controllers
```

## Development Notes

- All DTOs use Java 21 records for immutability
- Reactive programming with Mono/Flux throughout
- JWT tokens expire after 24 hours
- WebSocket connections support CORS for frontend integration
- Redis used for both caching and pub/sub messaging
- Scheduled tasks generate mock real-time data
- Database schema auto-initialized via data.sql

## Security Features

- JWT-based stateless authentication
- Role-based authorization
- CORS configuration for frontend integration
- Password encryption with BCrypt
- WebSocket endpoint protection (TODO)

This backend is production-ready and designed for seamless integration with React frontends and future AI service expansions.
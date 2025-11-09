# Enterprise Deployment Guide

## Prerequisites

### Development Environment
- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL 15+ (for production)
- Redis 7+ (for production)

### Production Environment
- Kubernetes cluster (1.25+)
- Helm 3.0+
- Prometheus & Grafana
- Container registry access

## Local Development

```bash
# Start with Docker Compose
docker-compose up -d

# Or run locally
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Production Deployment

### 1. Build & Push Image
```bash
mvn clean package
docker build -t ai-agent-system:1.0.0 .
docker tag ai-agent-system:1.0.0 your-registry/ai-agent-system:1.0.0
docker push your-registry/ai-agent-system:1.0.0
```

### 2. Deploy to Kubernetes
```bash
# Create secrets
kubectl create secret generic db-secret \
  --from-literal=url=r2dbc:postgresql://postgres:5432/aiagentsystem \
  --from-literal=username=aiagent \
  --from-literal=password=your-password

kubectl create secret generic jwt-secret \
  --from-literal=secret=your-jwt-secret

# Deploy application
kubectl apply -f k8s/
```

### 3. Configure Monitoring
```bash
# Deploy Prometheus & Grafana
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm install prometheus prometheus-community/kube-prometheus-stack
```

## Environment Variables

### Required
- `DATABASE_URL`: Database connection string
- `DATABASE_USERNAME`: Database username
- `DATABASE_PASSWORD`: Database password
- `JWT_SECRET`: JWT signing secret (min 32 chars)

### Optional
- `REDIS_ENABLED`: Enable Redis (default: false)
- `REDIS_HOST`: Redis host (default: localhost)
- `LOG_LEVEL`: Logging level (default: INFO)
- `RATE_LIMIT_RPM`: Rate limit per minute (default: 1000)

## Health Checks

- **Liveness**: `/actuator/health/liveness`
- **Readiness**: `/actuator/health/readiness`
- **Metrics**: `/actuator/prometheus`

## Security Considerations

1. **Secrets Management**: Use Kubernetes secrets or external secret managers
2. **Network Policies**: Implement pod-to-pod communication restrictions
3. **RBAC**: Configure role-based access control
4. **Image Scanning**: Scan container images for vulnerabilities
5. **TLS**: Enable TLS termination at ingress level

## Scaling

### Horizontal Pod Autoscaler
```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: ai-agent-system-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: ai-agent-system
  minReplicas: 3
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

## Monitoring & Alerting

### Key Metrics
- HTTP request rate and latency
- Database connection pool usage
- JVM memory and GC metrics
- Circuit breaker states
- Custom business metrics

### Alerts
- High error rate (>5%)
- High response time (>2s p95)
- Database connectivity issues
- Memory usage >80%
- Circuit breaker open state

## Backup & Recovery

### Database Backup
```bash
# Automated backup script
kubectl create cronjob postgres-backup \
  --image=postgres:15 \
  --schedule="0 2 * * *" \
  -- pg_dump -h postgres -U aiagent aiagentsystem > backup.sql
```

### Disaster Recovery
1. Database restoration from backups
2. Application state recovery via Redis persistence
3. Configuration restoration from Git

## Performance Tuning

### JVM Options
```bash
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:+UseStringDeduplication
-XX:+UseContainerSupport
-XX:MaxRAMPercentage=75.0
```

### Database Optimization
- Connection pooling (max 50 connections)
- Query optimization and indexing
- Read replicas for analytics workloads

## Troubleshooting

### Common Issues
1. **Database Connection**: Check network policies and credentials
2. **Memory Issues**: Adjust JVM heap settings
3. **High Latency**: Check database query performance
4. **Circuit Breaker**: Monitor external service health

### Debug Commands
```bash
# Check application logs
kubectl logs -f deployment/ai-agent-system

# Check metrics
curl http://localhost:8080/actuator/metrics

# Database health
kubectl exec -it postgres-pod -- psql -U aiagent -d aiagentsystem -c "SELECT 1"
```
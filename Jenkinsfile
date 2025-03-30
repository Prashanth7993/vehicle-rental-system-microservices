pipeline {
    agent any

    triggers {
        githubPush()
    }

    environment {
        REGISTRY = "hub.docker.com" // Replace with Docker Hub, ECR, or GCR
        IMAGE_TAG = "latest"
        HELM_RELEASE = "microservices"
        HELM_CHART_PATH = "./my-microservices-chart"
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/Prashanth7993/vehicle-rental-system-microservices.git'
            }
        }

        stage('Build & Push Docker Images') {
            steps {
                script {
                    def services = ["api-gateway", "authentication-service", "bookings-service", "config-server", "coupon-service", "coupon-service", "customer-support", "documents-service", "mail-service", "node-test", "payment-service", "review-service", "service-registry", "user-service", "vehicles-service", "vendor-service" ] // 12 microservices
                    for (s in services) {
                        sh """
                            docker build -t $REGISTRY/$s:$IMAGE_TAG ./$s
                            docker push $REGISTRY/$s:$IMAGE_TAG
                        """
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh """
                    helm upgrade --install $HELM_RELEASE $HELM_CHART_PATH
                """
            }
        }
    }

    post {
        success {
            echo "Deployment Successful!"
        }
        failure {
            echo "Deployment Failed! Check logs."
        }
    }
}

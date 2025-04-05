pipeline {
    agent {label 'jenkins-slave1'}

    triggers {
        githubPush()
    }

    environment {
        REGISTRY = "vehicle-rental-system-microservices" // Replace with Docker Hub, ECR, or GCR
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
	stage('Build & Push Docker Images Frontend'){
            steps{

                     withCredentials([usernamePassword(credentialsId: 'Docker_Hub_Credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]){
                        sh """
                        cd onthego
                        docker build -t $DOCKER_USER/frontend-onthego:v2 .
                        docker login -u $DOCKER_USER -p $DOCKER_PASS
                        docker push $DOCKER_USER/frontend-onthego:v2
                        """
                     }

            }
        }
        stage('Build & Push Docker Images Backend') {
            steps {
                script {
                    def services = ["api-gateway", "authentication-service", "bookings-service", "config-server", "coupon-service", "coupon-service", "customer-support",  "mail-service", "payment-service", "review-service", "service-registry", "user-service", "vehicles-service", "vendor-service" ] // 12 microservices
		     withCredentials([usernamePassword(credentialsId: 'Docker_Hub_Credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
			for (s in services ) {
                        	sh """
                            		cd vehicle-rentals-microservices
	                            	docker build -t $DOCKER_USER/$s:$IMAGE_TAG ./$s
                                        docker login -u $DOCKER_USER -p $DOCKER_PASS
					docker push $DOCKER_USER/$s:$IMAGE_TAG
                        	   """
                        }
                     }
                }
            }
        }

        stage('Deploying backend kubernetes with Helm') {
            steps {
                sh """
                    helm upgrade --install Microservice-Chart .
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

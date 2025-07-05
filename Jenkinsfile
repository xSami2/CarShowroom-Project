def deployToEnvironment(environment, composeFileName, imageTag = null , machineIpAddress , password) {
    try {
        if (imageTag) {
            echo "Deploying to ${environment} with tag: ${imageTag}"
        }
        sh """
           sshpass -p '${password}' ssh -o StrictHostKeyChecking=no ${machineIpAddress} '
                cd carshowroom/ &&
                docker compose -f ${composeFileName} down --remove-orphans &&
                docker compose -f ${composeFileName} up -d
            '
        """
        echo "Successfully deployed to ${environment} environment"
    } catch (Exception e) {
        error "Failed to deploy to ${environment} environment: ${e.getMessage()}"
    }
}

pipeline {
  agent any

  environment {
    // Docker Image Name
    IMAGE_NAME = 'carshowroom-project'
    // Docker Registry URL ( Infrabase Server)
    REGISTRY = '192.168.100.16:5000'
    // Full image name combining registry and image name
    FULL_IMAGE_NAME = "${REGISTRY}/${IMAGE_NAME}"
     devServer = 'sami@192.168.100.17'
     stagingServer = 'sami@192.168.100.18'
     prodServer = 'sami@192.168.100.19'
  }



  stages {

    stage('Checkout') {
               steps {
                   checkout scm
               }
           }

//     stage('Test') {
//         steps {
//             sh 'nohup java -jar target/developerChallenge-0.0.1-SNAPSHOT.jar > app.log &'
//             echo 'Waiting for app to be healthy...'
//             sh '''
//               for i in {1..10}
//               do
//                 curl --fail http://localhost:9091 && exit 0 || sleep 5
//               done
//               echo "App is not healthy after waiting."
//               exit 1
//             '''
//         }
//     }


  stage('Build Docker Image') {
              steps {
                  script {

                   def imageTag

                              // Determine tag based on branch
                              switch(env.BRANCH_NAME) {
                                  case 'dev':
                                      imageTag = 'dev'
                                      break
                                  case 'staging':
                                      imageTag = 'staging'
                                      break
                                  case 'main':
                                      imageTag = 'production'
                                      break
                                  default:
                                      imageTag = BRANCH_NAME
                                      echo "Warning: Unknown branch ${env.BRANCH_NAME}, using '${BRANCH_NAME}' tag"
                              }

                                echo "Building Docker image ${env.REGISTRY}/${env.IMAGE_NAME}:${imageTag}"
                                def appImage = docker.build("${env.REGISTRY}/${env.IMAGE_NAME}:${imageTag}")
                                    appImage.push()



                  }
              }
          }

    stage('Deploy') {
      steps {
        sh 'echo "Deploy script here depending on branch and env"'
        script{

            QA_SSH_PASS = credentials('qa-ssh-password') // 👈 inject the password here
          switch(env.BRANCH_NAME) {
              case 'dev':
                  imageTag = 'dev'
                  deployToEnvironment('dev', 'docker-compose.qa.yaml' , imageTag , env.devServer , QA_SSH_PASS)
                  break
              case 'staging':
                  imageTag = 'staging'
                  deployToEnvironment('staging', 'docker-compose.staging.yaml', imageTag , env.stagingServer)
                  break
              case 'main':
                  imageTag = 'production'
                  deployToEnvironment('production', 'docker-compose.prod.yaml', imageTag , env.prodServer)
                  break
              default:
                  imageTag = BRANCH_NAME
                  echo "Warning: Unknown branch ${env.BRANCH_NAME}, using '${BRANCH_NAME}' tag"
                  echo "No deployment configured for branch: ${env.BRANCH_NAME}"
          }
        }
      }
    }
  }

   post {
          success {
              echo "Pipeline completed successfully."
          }
          failure {
              echo "Pipeline failed."
          }
      }
}

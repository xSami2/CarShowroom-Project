pipeline {
  agent any

  environment {
    // Docker Image Name
    IMAGE_NAME = 'CarShowroom-project'
    // Docker Registry URL ( Infrabase Server)
    REGISTRY = '192.168.100.16:5000'
    // Full image name combining registry and image name
    FULL_IMAGE_NAME = "${REGISTRY}/${IMAGE_NAME}"

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
                                  case 'stag':
                                      imageTag = 'staging'
                                      break
                                  case 'prod':
                                      imageTag = 'latest'
                                      break
                                  default:
                                      imageTag = 'unknown'
                                      echo "Warning: Unknown branch ${env.BRANCH_NAME}, using 'unknown' tag"
                              }

                                echo "Building Docker image ${env.REGISTRY}/${env.IMAGE_NAME}:${imageTag}"
                                def appImage = docker.build("${env.REGISTRY}/${env.IMAGE_NAME}:${imageTag}")
                                docker.push

                      // Build the Docker image and tag it with registry URL and image name
                      def appImage = docker.build("${env.REGISTRY}/${env.IMAGE_NAME}:${branchTag}")
                        appImage.push()


                  }
              }
          }

    stage('Deploy') {
      steps {
        sh 'echo "Deploy script here depending on branch and env"'
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

pipeline {
    agent any

    tools {
        maven 'maven-3.3.9'
    }

    options {
        parallelsAlwaysFailFast()
    }

    parameters {
        // choice(
        //     name: 'ENVIRONMENT',
        //     choices: ['alpha', 'beta', 'c1', 'prod'],
        //     description: 'Select ENV'
        // )
        gitParameter(
            name: 'BRANCH',
            branchFilter: 'origin/(.*)',
            defaultValue: 'master',
            sortMode: 'ASCENDING_SMART',
            type: 'PT_BRANCH',
            selectedValue: 'TOP',
            quickFilterEnabled: true
        )
        // booleanParam(
        //     name: 'CLEAN_NPM',
        //     defaultValue: true,
        //     description: 'Delete all from node_modules folder.'
        // )
        // booleanParam(
        //     name: 'SKIP_TEST',
        //     defaultValue: false,
        //     description: 'Should it run the unit tests?'
        // )
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: "${params.BRANCH}", credentialsId: "${env.GIT_CREDENTIALS_ID}", url: "${env.GIT_URL}"
            }

        }

        stage('Compile') {
            steps {
                    java: {
                        sh label: "Compile Java code", script: "mvn clean install"
                    }
            }
        }

        stage('Package') {
            steps {
                    java: {
                        sh label: "Package Java code", script: "mvn package"
                    }
            }
        }

        stage("Publish to nexus") {
            when {
                branch 'develop'
            }
            steps {
                script {
                    // Read POM xml file using 'readMavenPom' step
                    pom = readMavenPom file: "pom.xml";
                    // Find built artifact under target folder
                    filesByGlob = findFiles(glob: "target/*.jar");
                    // Print some info from the artifact found
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    // Extract the path from the File found
                    artifactPath = filesByGlob[0].path;
                    // Assign to a boolean response verifying If the artifact name exists
                    artifactExists = fileExists artifactPath;

                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";

                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: pom.version,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                // Artifact generated such as .jar, .ear and .war files.
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging],

                                // Lets upload the pom.xml file for additional information for Transitive dependencies
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: "pom.xml",
                                type: "pom"]
                            ]
                        );

                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                    }
                }
            }
        }

    }


    post {
        always {
            howtankNotification (
                streamId: 'ccb5e47a4f0311ea909c0a815897bad6ae46634d',
                message: 'Hey @all! ${JOB_NAME} build status from $BRANCH branch is ${BUILD_STATUS}',
                accessToken: 'id:howtank_jenkins_jwt',
                notifyAborted: 'false',
                notifyFailure: 'true',
                notifyNotBuilt: 'false',
                notifySuccess: 'true',
                notifyUnstable: 'false',
                notifyBackToNormal: 'true'
            )
            recordIssues(
                enabledForFailure: true, aggregatingResults: true,
                tools: [java(), checkStyle(pattern: 'babhelp/web/target/eslint.xml'), findBugs(pattern: 'babhelp/web/target/findbugsXml.xml') ]
            )
            jacoco classPattern: '**/target/classes', exclusionPattern: '**/*Test*.class', execPattern: '**/target/jacoco.exec', inclusionPattern: '**/*.class'
        }
    }
}

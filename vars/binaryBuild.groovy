def call(Map args){
    def config = pipelineCfg()
    container ('aws-cli'){
        checkout changelog: true, poll: true, scm: [
        $class: 'GitSCM',
        branches: [[name: "binary-build-deployment"]],
        doGenerateSubmoduleConfigurations: false,
        submoduleCfg: [],
        userRemoteConfigs: [[credentialsId: '6c8b6848-ca10-4862-b1a9-fe3e6d46da61', url: "${config.repo_url}"]]
        ]
        sh 'ls'
        sh """
            pwd
            wget https://dl.google.com/go/go1.18.linux-amd64.tar.gz
            tar -C /usr/local -xzf go1.18.linux-amd64.tar.gz
            export PATH=$PATH:/usr/local/go/bin
            go version
            export AWS_ACCESS_KEY_ID=YFRP3PS4LIJEOZVRUMMK
            export AWS_SECRET_ACCESS_KEY=0s4FQ470cF9AGDg7old5fLyvvhbhnqO99ooruvQdVOs
            export AWS_DEFAULT_REGION=sgp1

            git config --global url."https://afsanarozanaufal:glpat-fhyFdTnzjm-sQJ4epsXK@gitlab.com/kds-platform/plugin.git".insteadOf "https://gitlab.com/kds-platform/plugin.git"

            go mod download
            go mod verify
            go mod tidy -v 
            go build -o ${config.service_name}

            
            ls -la
            
            apt install zip
            curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
            unzip awscliv2.zip
            ./aws/install

            aws s3 cp ${config.service_name} --endpoint-url ${config.spaces_url} s3://binary-build/beta
        """
    }
    sh "cd /var/run; ls"
    // script {
    //     sshPublisher(
    //         publishers: [
    //             sshPublisherDesc(
    //                 configName: 'vm-do-testing',
    //                 verbose: true,
    //                 transfers: [
    //                     sshTransfer(
    //                         remoteDirectory: "/home",
    //                         execCommand: """
    //                         export PATH=$PATH:/usr/local/go/bin;
    //                         export AWS_ACCESS_KEY_ID=YFRP3PS4LIJEOZVRUMMK;
    //                         export AWS_SECRET_ACCESS_KEY=0s4FQ470cF9AGDg7old5fLyvvhbhnqO99ooruvQdVOs;
    //                         export AWS_DEFAULT_REGION=sgp1;

    //                         git clone https://automation:glpat-Vf6rMnhFzEbshHrj2TYQ@gitlab.com/kliklab/automation-platform/services-platform/example-service.git;
    //                         cd example-service;
    //                         git checkout binary-build-deployment;
    //                         go version;

    //                         go mod download;
    //                         go mod verify;
    //                         go mod tidy -v; 
    //                         go build -o ${config.service_name}-${config.tag};

    //                         aws s3 cp ${config.service_name} --endpoint-url ${config.spaces_url} s3://binary-build/beta
    //                         """,
    //                         execTimeout: 60000  
    //                     )
    //                 ]
    //             )
    //         ]
    //     )
    // }
    sh "echo binary build"
}


def pushChart(Map args) {
    echo "Push Chart"
    sh "aws s3 cp ${args.service_name}-*.tgz --endpoint-url ${args.spaces_url} s3://helm-charts/${args.namespace}/beta/"
}


// go mod download
// go mod verify
// go mod tidy -v 
def call(Map envar) {
    echo "language ${envar.language}"

    switch(envar.language) {
        case "golang":
            echo "trigger golang build"
            goBuild(envar)   
            break;
        case "node":
            echo "trigger npm build"
            npmBuild(envar)
            break;
        case "maven":
            echo "trigger maven build"
            mvnBuild(envar)
            break;
    }
}
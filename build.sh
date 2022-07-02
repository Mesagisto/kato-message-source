build(){
	rm -rf packages
	mkdir -p packages
	export NO_KT="false"
	./gradlew clean pkg
	mv build/pkg/*.jar packages/kato.jar
	export NO_KT="true"
	./gradlew clean pkg
	mv build/pkg/*.jar packages/kato-NOKT.jar
}
build

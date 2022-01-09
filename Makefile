all: linux-x86 linux-x86_64 windows-x86_64 mac-x86_64
compile: gradlew
	./gradlew clean pkg
	rm -rf packages
	mkdir -p packages
	mv build/pkg/*.jar packages/
	mv packages/*.jar packages/kato-all.jar
extract: compile
	rm -rf extract
	mkdir -p extract
	unzip packages/kato-all.jar -d extract/ > /dev/null
linux-x86_64: target := target/linux-x86_64
linux-x86_64: name := kato-linux-x86_64.jar
linux-x86_64: extract
	mkdir -p $(target)/extract $(target)/tmp
	cp -r extract $(target)
	# rocksdb
	mv $(target)/extract/librocksdbjni-linux64.so $(target)/tmp
	rm $(target)/extract/librocksdbjni*
	mv $(target)/tmp/librocksdbjni-linux64.so $(target)/extract
	# package
	jar -c -f packages/$(name) -C $(target)/extract/ .
	rm -rf $(target)
linux-x86: target := target/linux-x86
linux-x86: name := kato-linux-x86.jar
linux-x86: extract
	mkdir -p $(target)/extract $(target)/tmp
	cp -r extract $(target)
	# rocksdb
	mv $(target)/extract/librocksdbjni-linux32.so $(target)/tmp
	rm $(target)/extract/librocksdbjni*
	mv $(target)/tmp/librocksdbjni-linux32*.so $(target)/extract
	# package
	jar -c -f packages/$(name) -C $(target)/extract/ .
	rm -rf $(target)
windows-x86_64: target := target/windows-x86_64
windows-x86_64: name := kato-windows-x86_64.jar
windows-x86_64: extract
	mkdir -p $(target)/extract $(target)/tmp
	cp -r extract $(target)
	# rocksdb
	mv $(target)/extract/librocksdbjni-win64.dll $(target)/tmp
	rm $(target)/extract/librocksdbjni*
	mv $(target)/tmp/librocksdbjni-win64.dll $(target)/extract
	# package
	jar -c -f packages/$(name) -C $(target)/extract/ .
	rm -rf $(target)
mac-x86_64: target := target/mac-x86_64
mac-x86_64: name := kato-mac-x86_64.jar
mac-x86_64: extract
	mkdir -p $(target)/extract $(target)/tmp
	cp -r extract $(target)
	# rocksdb
	mv $(target)/extract/librocksdbjni-osx.jnilib $(target)/tmp
	rm $(target)/extract/librocksdbjni*
	mv $(target)/tmp/librocksdbjni-osx.jnilib $(target)/extract
	# package
	jar -c -f packages/$(name) -C $(target)/extract/ .
	rm -rf $(target)
clean:
	rm -rf extract target


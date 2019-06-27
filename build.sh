#!/usr/bin/env bash
clear
export PATH=/home/kavan/Applications/kotlin-native-linux-1.3/bin:$PATH
export PATH=/home/kavan/Applications/kotlin-native-linux-1.3/jdk-12/bin:$PATH

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

IPREFIX_macbook=-I/opt/local/include
IPREFIX_linux=-I/usr/include

if [ x$TARGET == x ]; then
case "$OSTYPE" in
  darwin*)  TARGET=macbook ;;
  linux*)   TARGET=linux ;;
  *)        echo "unknown: $OSTYPE" && exit 1;;
esac
fi

var=IPREFIX_${TARGET}
IPREFIX="${!var}"


if [ ! -f $DIR/gtk3.bc.klib ]; then
  echo "Generating GTK stubs (once), may take few mins depending on the hardware..."
  cinterop -J-Xmx8g \
  -compiler-options $IPREFIX/gtk-3.0 \
  -compiler-options $IPREFIX/glib-2.0 \
  -compiler-options $IPREFIX/pango-1.0 \
  -compiler-options $IPREFIX/cairo \
  -compiler-options $IPREFIX/gdk-pixbuf-2.0 \
  -compiler-options $IPREFIX/atk-1.0 \
  -def $DIR/gtk3.def \
  -target $TARGET -o $DIR/gtk3.bc || exit 1
fi



konanc -target $TARGET $DIR/src -library $DIR/gtk3.bc.klib -o $DIR/Gtk3Demo.kexe -opt || exit 1

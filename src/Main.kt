/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

//package sample.gtk

import kotlinx.cinterop.*
import gtk3.*


fun main(args: Array<String>) {

    memScoped {
        val argc = alloc<IntVar>()
        argc.value = args.size
        val argv = alloc<CPointerVar<CPointerVar<ByteVar>>>()
        argv.value = args.map { it.cstr.ptr }.toCValues().ptr
        gtk_init(argc.ptr, argv.ptr)
    }

    /* Construct a GtkBuilder instance and load our UI description */
    val builder = gtk_builder_new()
    val isLoaded = gtk_builder_add_from_file (builder, "builder.ui", null)
    if(isLoaded == 0u) {
	println("Error loadding .ui file")
        return
    }

    /* Connect signal handlers to the constructed widgets. */
    val window = gtk_builder_get_object(builder, "window")!!
    g_signal_connect_data(window.reinterpret(), "destroy", 
    staticCFunction { widget: CPointer<GtkWidget>? -> gtk_widget_destroy(widget) }.reinterpret(),
    null, null, 0u)


    val button1 = gtk_builder_get_object(builder, "button1")!!
    g_signal_connect_data(button1.reinterpret(), "clicked", 
    staticCFunction { _: CPointer<GtkWidget>?, _: gpointer? -> println("Button 1 clicked!") }.reinterpret(),
    null, null, 0u)


    val button2 = gtk_builder_get_object (builder, "button2")!!
    g_signal_connect_data(button2.reinterpret(), "clicked", 
    staticCFunction { _: CPointer<GtkWidget>?, _: gpointer? -> println("Button 2 clicked!") }.reinterpret(),
    null, null, 0u)


    val button3 = gtk_builder_get_object(builder, "quit")!!
    g_signal_connect_data(button3.reinterpret(), "clicked", 
    staticCFunction { _: CPointer<GtkWidget>? -> gtk_main_quit() }.reinterpret(),
    null, null, 0u)


    gtk_main()

    return

}

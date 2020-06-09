package com.qwwuyu.example.utils.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class VoidTypeAdapter extends TypeAdapter<Void> {
    @Override
    public void write(final JsonWriter out, final Void value) throws IOException {
        out.nullValue();
    }

    @Override
    public Void read(final JsonReader in) throws IOException {
        in.skipValue();
        return null;
    }
}
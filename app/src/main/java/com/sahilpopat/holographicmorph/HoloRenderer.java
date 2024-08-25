package com.sahilpopat.holographicmorph;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class HoloRenderer implements GLSurfaceView.Renderer {

    private final FloatBuffer vertexBuffer;
    private int program;

    public float[] quadVertices = {
            // X, Y, Z
            -1.0f,  1.0f, 0.0f,  // Top-left
            -1.0f, -1.0f, 0.0f,  // Bottom-left
            1.0f,  1.0f, 0.0f,  // Top-right
            1.0f, -1.0f, 0.0f   // Bottom-right
    };

    public HoloRenderer() {
        // Initialize the vertex buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(quadVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(quadVertices);
        vertexBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Initialize OpenGL settings
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Load and compile shaders
        String vertexShaderCode = "attribute vec4 position;" +
                "uniform mat4 modelMatrix;" +
                "void main() {" +
                "gl_Position = modelMatrix * position;" +
                "}";
        String fragmentShaderCode = "precision mediump float;" +
                "void main() {" +
                "gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);" +
                "}";

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(program);
        int positionHandle = GLES20.glGetAttribLocation(program, "position");
        int modelMatrixHandle = GLES20.glGetUniformLocation(program, "modelMatrix");

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // Render each quadrant
        renderQuadrant(-1.0f, 1.0f, 0.5f, modelMatrixHandle);
        renderQuadrant(1.0f, 1.0f, 0.5f, modelMatrixHandle);
        renderQuadrant(-1.0f, -1.0f, 0.5f, modelMatrixHandle);
        renderQuadrant(1.0f, -1.0f, 0.5f, modelMatrixHandle);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    private void renderQuadrant(float x, float y, float scale, int modelMatrixHandle) {
        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, 0);
        Matrix.scaleM(modelMatrix, 0, scale, scale, 1.0f);

        GLES20.glUniformMatrix4fv(modelMatrixHandle, 1, false, modelMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
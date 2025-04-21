package com.kaanbicakci.battleblocks.core;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.*;

public class LevelEditorScene extends Scene {
    private String vertexShaderSrc = "#version 330 core\n" +
                                     "layout(location=0) in vec3 aPos;\n" +
                                     "layout(location=1) in vec4 aColor;\n" +
                                     "\n" +
                                     "out vec4 fColor;\n" +
                                     "\n" +
                                     "void main()\n" +
                                     "{\n" +
                                     "    fColor = aColor;\n" +
                                     "    gl_Position = vec4(aPos, 1.0);\n" +
                                     "}\n";
    private String fragmentShaderSrc = "#version 330 core\n" +
                                       "\n" +
                                       "in vec4 fColor;\n" +
                                       "\n" +
                                       "out vec4 color;\n" +
                                       "\n" +
                                       "void main()\n" +
                                       "{\n" +
                                       "    color = fColor;\n" +
                                       "}\n";

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
            //pos                   //color
            0.5f, -.5f, 0f,      1f, 0f, 0f, 1f, // br 0
            -.5f, 0.5f, 0f,      0f, 1f, 0f, 1f, // tl 1
            0.5f, 0.5f, 0f,      0f, 0f, 1f, 1f, // tr 2
            -.5f, -.5f, 0f,      1f, 1f, 0f, 1f, // bl 3
    };

    // IMPORTANT must be in counterclockwise
    private int[] elementArray = {
            2, 1, 0,
            0, 1, 3
    };

    private int vaoID, vboID, eboID;

    public LevelEditorScene() {
    }

    @Override
    public void init() {
        // Compile and link shaders

        // Load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);
        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: defaultShader.glsl\n\tVertex Shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);
        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: defaultShader.glsl\n\tFragment Shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);
        // Check linker errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("Error: defaultShader.glsl\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

        // Generate VAO, VBO, EBO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        // Create float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO and upload vertex buffer
        vboID = glGenVertexArrays();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // add the vertex attr pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float deltaTime) {
        // Bind shader program
        glUseProgram(shaderProgram);
        // Bind the VAO
        glBindVertexArray(vaoID);

        // Enable vertex attr pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        glUseProgram(0);
    }
}

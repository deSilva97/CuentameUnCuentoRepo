package es.unir.cuentameuncuento.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioFileConverter {

    public static File convertBytesToAudioFile(byte[] speechBytes) {
        try {

            File audioFile = File.createTempFile("audio", ".wav");

            try (FileOutputStream fos = new FileOutputStream(audioFile)) {
                fos.write(speechBytes);
            }

            return audioFile;
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

}

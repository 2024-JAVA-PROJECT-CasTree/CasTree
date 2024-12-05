package MainLogin;

import javax.imageio.IIOException;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class BGMPlayer extends Thread{
    private String bgmPath;
    private boolean loop;  // 음악 반복재생 여부

    public BGMPlayer(String bgmPath, boolean loop){
        this.bgmPath = bgmPath;
        this.loop = loop;
    }

    @Override
    public void run() {
        try{

            // 리소스를 스트림으로 로드
            InputStream inputStream = getClass().getResourceAsStream(bgmPath);
            if (inputStream == null) {
                throw new IOException("Cannot find resource: " + bgmPath);
            }

            // 오디오 파일 로드
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);

            // 오디오 클립 생성
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            if(loop){
                clip.loop(Clip.LOOP_CONTINUOUSLY); // 반복 재생
            }else {
                clip.start(); // 한 번만 재생
            }

            // Clip이 종료될 때까지 대기
            clip.drain();
        }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){
            e.printStackTrace();
        }
    }
}

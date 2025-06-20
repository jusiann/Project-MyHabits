package com.adil.ProjectMyHabits;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoadingController {
    @FXML
    private ProgressBar pbLoading;
    @FXML
    private ImageView imgLogo;
    @FXML
    private Label lblHint;
    private final String[] messages = {
    	    "Make today count!", // Bugünü değerli kıl!
    	    "Seize the day!", // Günü yakala!
    	    "It's a good day to have a good day.", // İyi bir gün geçirmek için güzel bir gün.

    	    "You are the master of your habits.", // Alışkanlıklarının ustası sensin.
    	    "Habits shape destiny—start shaping yours.", // Alışkanlıklar kaderi belirler—seninkini şekillendirmeye başla.
    	    "Build habits that build you.", // Seni inşa eden alışkanlıklar geliştir.

    	    "You're on the right track!", // Doğru yoldasın!
    	    "Progress looks good on you!", // İlerleme sana çok yakışıyor!
    	    "Keep crushing it!", // Harikalar yaratmaya devam et!

    	    "Eyes on the prize—keep going!", // Hedefe odaklan—devam et!
    	    "Stay the course!", // Rotanı bozma!
    	    "No quit in you—keep pushing!", // Vazgeçmek yok—devam et!

    	    "Little by little, a little becomes a lot.", // Azar azar, az çok olur.
    	    "Small steps, big changes.", // Küçük adımlar, büyük değişimler.
    	    "One small step at a time.", // Her seferinde bir küçük adım.

    	    "Consistency is key.", // İstikrar en önemli şeydir.
    	    "Stick with it and success will follow.", // Devam et, başarı gelecektir.
    	    "Winners show up every day.", // Kazananlar her gün ortaya çıkar.

    	    "Keep moving forward.", // İleriye gitmeye devam et.
    	    "Step by step, you're getting there.", // Adım adım hedefe yaklaşıyorsun.
    	    "Forward is forward, no matter how small.", // Ne kadar küçük olursa olsun, ilerlemek ilerlemektir.
    };

    @FXML
    public void initialize() {
    	ScaleTransition scaleTransitionLogo = new ScaleTransition(Duration.seconds(1), imgLogo);
     	scaleTransitionLogo.setFromX(1.0);
    	scaleTransitionLogo.setToX(1.1);
    	scaleTransitionLogo.setFromY(1.0);
        scaleTransitionLogo.setToY(1.1);    
        scaleTransitionLogo.setCycleCount(Timeline.INDEFINITE);
        scaleTransitionLogo.setAutoReverse(true); 
        scaleTransitionLogo.play();
        
        int randomMessages = (int) (Math.random() * messages.length);
        lblHint.setText(messages[randomMessages]);
        
        final double[] progressLoading = {0};
    	pbLoading.setProgress(0);
    	
    	Timeline timeLineLoading = new Timeline();
        timeLineLoading.setCycleCount(100);
        timeLineLoading.getKeyFrames().add(new KeyFrame(Duration.millis(20), event -> {
            progressLoading[0] += 0.01;
            pbLoading.setProgress(progressLoading[0]);
            if (progressLoading[0] >= 1.0) {
            	loadLoginStage();
            }
        }));
        timeLineLoading.play();
    }
    
    private void loadLoginStage() {
        try {
        	Stage loginStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            loginStage.setTitle("My Habits");
            loginStage.initStyle(StageStyle.DECORATED);
            loginStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/iconMyHabits.jpg")));
            loginStage.setResizable(false);
            loginStage.setScene(scene);
            loginStage.show();
            
            Stage currentStage = (Stage) pbLoading.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.example.imagemachine.modal;

public class Image {
    private int ImageId;
    private String ImageMachine;
    private String ImageUri;

    public Image(int imageId, String imageMachine, String imageUri) {
        ImageId = imageId;
        ImageMachine = imageMachine;
        ImageUri = imageUri;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public String getImageMachine() {
        return ImageMachine;
    }

    public void setImageMachine(String imageMachine) {
        ImageMachine = imageMachine;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }
}

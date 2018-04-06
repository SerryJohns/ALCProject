package com.serionz.alcproject.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

/**
 * Created by johnpaulseremba on 05/04/2018.
 */

public class Emojifier {

    private static final String TAG = Emojifier.class.getSimpleName();
    private static double EYE_OPEN_THRESHOLD = .68;
    private static double SMILING_THRESHOLD = .5;

    static void detectFaces(Context context, Bitmap picture) {

        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        Frame frame = new Frame.Builder().setBitmap(picture).build();

        // Detect faces
        SparseArray<Face> faces = detector.detect(frame);
        Log.d(TAG, "Detected faces: " + faces.size());

        if (faces.size() == 0) {
            Toast.makeText(context, "No faces detected!", Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < faces.size(); i++) {
            int key = faces.keyAt(i);
            whichEmoji(faces.get(key));
        }

        // Release detector
        detector.release();
    }

    public enum Emoji {
        CLOSED_FROWN,
        CLOSED_SMILE,
        FROWN,
        LEFT_WINK,
        LEFT_WINK_FROWN,
        RIGHT_WINK,
        RIGHT_WINK_FROWN,
        SMILE
    }

    private static void whichEmoji(Face face) {
        Emoji emoji;
        boolean leftEyeOpen = face.getIsRightEyeOpenProbability() > EYE_OPEN_THRESHOLD;
        boolean rightEyeOpen = face.getIsLeftEyeOpenProbability() > EYE_OPEN_THRESHOLD;
        boolean isSmiling = face.getIsSmilingProbability() > SMILING_THRESHOLD;

        if (isSmiling) {
            if (leftEyeOpen && rightEyeOpen) {
                emoji = Emoji.SMILE;
            } else if (leftEyeOpen && !rightEyeOpen) {
                emoji = Emoji.LEFT_WINK;
            } else if (!leftEyeOpen && rightEyeOpen) {
                emoji = Emoji.RIGHT_WINK;
            } else {
                emoji = Emoji.CLOSED_SMILE;
            }
        } else {
            if (leftEyeOpen && rightEyeOpen) {
                emoji = Emoji.FROWN;
            } else if (leftEyeOpen && !rightEyeOpen) {
                emoji = Emoji.LEFT_WINK_FROWN;
            } else if (!leftEyeOpen && rightEyeOpen) {
                emoji = Emoji.RIGHT_WINK_FROWN;
            } else {
                emoji = Emoji.CLOSED_FROWN;
            }
        }

        Log.d(TAG, "emoji: " + emoji.name());
    }

}

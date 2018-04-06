package com.serionz.alcproject.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.serionz.alcproject.R;

/**
 * Created by johnpaulseremba on 05/04/2018.
 */

public class Emojifier {

    private static final String TAG = Emojifier.class.getSimpleName();
    private static double EYE_OPEN_THRESHOLD = .68;
    private static double SMILING_THRESHOLD = .3;
    private static float EMOJI_SCALE_FACTOR = .9f;

    static Bitmap detectFacesAndOverlayEmoji(Context context, Bitmap picture) {

        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        Frame frame = new Frame.Builder().setBitmap(picture).build();

        // Detect faces
        SparseArray<Face> faces = detector.detect(frame);
        Log.d(TAG, "Detected faces: " + faces.size());

        Bitmap resultBitmap = picture;

        if (faces.size() == 0) {
            Toast.makeText(context, "No faces detected!", Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < faces.size(); i++) {
            Bitmap emojiBitmap;
            int key = faces.keyAt(i);
            Emoji selectedEmoji = whichEmoji(faces.get(key));

            switch (selectedEmoji) {
                case FROWN:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.frown);
                    break;
                case SMILE:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.smile);
                    break;
                case LEFT_WINK:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.leftwink);
                    break;
                case RIGHT_WINK:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.rightwink);
                    break;
                case CLOSED_FROWN:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.closed_frown);
                    break;
                case CLOSED_SMILE:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.closed_smile);
                    break;
                case LEFT_WINK_FROWN:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.leftwinkfrown);
                    break;
                case RIGHT_WINK_FROWN:
                    emojiBitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.rightwinkfrown);
                    break;
                default:
                    emojiBitmap = null;
                    Toast.makeText(context, "No emoji returned!", Toast.LENGTH_SHORT).show();
                    break;
            }

            resultBitmap = addBitmapToFace(resultBitmap, emojiBitmap, faces.get(key));
        }

        // Release detector
        detector.release();

        return resultBitmap;
    }

    private static Bitmap addBitmapToFace(Bitmap picture, Bitmap emojiBitmap, Face face) {
        Bitmap resultBitmap = Bitmap.createBitmap(picture.getWidth(),
                picture.getHeight(), picture.getConfig());

        float scaleFactor = EMOJI_SCALE_FACTOR;
        int newEmojiWidth = (int) (face.getWidth() * scaleFactor);
        int newEmojiHeight = (int) (emojiBitmap.getHeight() *
                newEmojiWidth / emojiBitmap.getWidth() * scaleFactor);

        // Scale the emoji
        emojiBitmap = Bitmap.createScaledBitmap(emojiBitmap, newEmojiWidth, newEmojiHeight, false);

        // Determine the emohi position so it best lines up with the face
        float emojiPositionX = (face.getPosition().x + face.getWidth() / 2) - emojiBitmap.getWidth() / 2;
        float emojiPositionY = (face.getPosition().y + face.getHeight() / 2) - emojiBitmap.getHeight() / 3;

        // Create a canvas and draw the bitmaps to it
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(picture, 0, 0, null);
        canvas.drawBitmap(emojiBitmap, emojiPositionX, emojiPositionY, null);

        return resultBitmap;
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

    private static Emoji whichEmoji(Face face) {
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

        return emoji;
    }

}

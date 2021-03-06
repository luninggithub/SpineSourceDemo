package com.baidu.duer.spine.model;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;

public class Raptor extends ApplicationAdapter {

    OrthographicCamera camera;
    PolygonSpriteBatch batch;
    SkeletonRenderer renderer;
    SkeletonRendererDebug debugRenderer;

    TextureAtlas atlas;
    Skeleton skeleton;
    AnimationState state;

    public void create () {
        camera = new OrthographicCamera();
        batch = new PolygonSpriteBatch(); // Required to render meshes. SpriteBatch can't render meshes.
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(true);
        debugRenderer = new SkeletonRendererDebug();
        debugRenderer.setMeshTriangles(false);
        debugRenderer.setRegionAttachments(false);
        debugRenderer.setMeshHull(false);

        atlas = new TextureAtlas(Gdx.files.internal("raptor/raptor-pma.atlas"));

        SkeletonJson loader = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
        // SkeletonLoader loader = new SkeletonBinary(atlas); // Or use SkeletonBinary to load binary data.
        loader.setScale(0.3f); // Load the skeleton at 50% the size it was in Spine.
        SkeletonData skeletonData = loader.readSkeletonData(Gdx.files.internal("raptor/raptor-pro.json"));

        skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        skeleton.setPosition(250, 20);

        AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.

        state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
        state.setTimeScale(0.6f); // Slow all animations down to 60% speed.

        // Queue animations on tracks 0 and 1.
        state.setAnimation(0, "walk", true);
        state.addAnimation(1, "gun-grab", false, 2); // Keys in higher tracks override the pose from lower tracks.
    }

    public void render () {
        state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.

        ScreenUtils.clear(0, 0, 0, 0);

        if (state.apply(skeleton)) // Poses skeleton using current animations. This sets the bones' local SRT.
            skeleton.updateWorldTransform(); // Uses the bones' local SRT to compute their world SRT.

        // Configure the camera, SpriteBatch, and SkeletonRendererDebug.
        camera.update();
        batch.getProjectionMatrix().set(camera.combined);
        debugRenderer.getShapeRenderer().setProjectionMatrix(camera.combined);

        batch.begin();
        renderer.draw(batch, skeleton); // Draw the skeleton images.
        batch.end();

        debugRenderer.draw(skeleton); // Draw debug lines.
    }

    public void resize (int width, int height) {
        camera.setToOrtho(false); // Update camera with new size.
    }

    public void dispose () {
        atlas.dispose();
    }

    public void setAnimate(String animate) {
        state.addAnimation(0, animate, true, 0);
    }
}

package com.baidu.duer.spine.model;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.baidu.duer.spine.util.LogUtil;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.BoneData;
import com.esotericsoftware.spine.Dress;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.SlotData;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;

import java.util.ArrayList;
import java.util.List;


public class LimiShow2 extends ApplicationAdapter {
    private static final String TAG = "LimiShow2";

    OrthographicCamera camera;
//    SpriteBatch batch;
    PolygonSpriteBatch batch;
    SkeletonRenderer renderer;
    SkeletonRendererDebug debugRenderer;
    TextureAtlas atlas;
    Skeleton skeleton;
    AnimationState state;
    SkeletonJson json;
    private SkeletonData skeletonData;

    @Override
    public void create() {
        camera = new OrthographicCamera();
//        batch = new SpriteBatch();
        batch = new PolygonSpriteBatch();
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(false); // PMA results in correct blending without outlines.
        debugRenderer = new SkeletonRendererDebug();
        debugRenderer.setBoundingBoxes(false);
        debugRenderer.setRegionAttachments(false);
        atlas = new TextureAtlas(Gdx.files.internal("limishow/role.atlas"));
        json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
        json.setScale(1.0f); // Load the skeleton at 60% the size it was in Spine.
        skeletonData = json.readSkeletonData(Gdx.files.internal("limishow/role.json"));
        skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        skeleton.setPosition(175, 0);
        int[] dressList = readConfig();
        addGroupDress(dressList);

//        AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
//        state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
//        state.setTimeScale(1.0f); // Slow all animations down to 50% speed.
    }

    /**
     * 根据config列表换装
     * @param dressList 装扮列表
     */
    private void addGroupDress(int[] dressList) {
        for (Integer dressId : dressList) {
            FileHandle imageHandle = Gdx.files.internal("limishow/dress/" + dressId);
            FileHandle atlasHandle = Gdx.files.internal("limishow/dress/" + dressId + "/dress.atlas");
            TextureAtlas dress = new TextureAtlas(atlasHandle, imageHandle);
            AtlasAttachmentLoader loader = new AtlasAttachmentLoader(dress);
            SkeletonJson skeletonJson = new SkeletonJson(loader);
//            SkeletonData dressSkeletonData = loader.readSkeletonData(Gdx.files.internal("limishow/dress/" + dressId + "/dress.json"));
            FileHandle fileHandle = Gdx.files.internal("limishow/dress/" + dressId + "/dress.json");
            JsonValue root = new JsonReader().parse(fileHandle);
            JsonValue attachments = root.get("attachments");
            LogUtil.i(TAG, "Dress id is:" + dressId);
            List<Dress> dataList = skeletonJson.updateAttachment(skeletonData, attachments);
            Array<Slot> slots = skeleton.getSlots();
            for (Dress data : dataList) {
                Slot slot = slots.get(data.slotIndex);
                slot.getData().setAttachmentName(data.attachmentName);
            }
            skeleton.setToSetupPose();
        }
    }

    /**
     * 读取装扮的配置文件
     * @return
     */
    private int[] readConfig() {
        FileHandle fileHandle = Gdx.files.internal("limishow/config.json");
        JsonValue root = new JsonReader().parse(fileHandle);
        int[] dressIds = root.get("dressIds").asIntArray();
        LogUtil.i(TAG, "Read config length is:" + dressIds.length);
        for (Integer v : dressIds) {
            LogUtil.i(TAG, "Read config is:" + v);
        }
        return dressIds;
    }

    /**
     * 执行指定动作
     */
    public void changeAction() {
        FileHandle imageHandle = Gdx.files.internal("limishow/dance/action");
        FileHandle atlasHandle = Gdx.files.internal("limishow/dance/action/action.atlas");
        TextureAtlas action = new TextureAtlas(atlasHandle, imageHandle);

        AtlasAttachmentLoader loader = new AtlasAttachmentLoader(action);
        SkeletonJson skeletonJson = new SkeletonJson(loader);

        FileHandle fileHandle = Gdx.files.internal("limishow/dance/action/action.json");
        JsonValue root = new JsonReader().parse(fileHandle);
        JsonValue bones = root.get("bones");
        List<BoneData> newBoneDatas = skeletonJson.addNewBoneDatas(skeletonData, bones);
        skeleton.addNewBones(newBoneDatas);
        skeleton.updateCache();
        skeleton.updateWorldTransform();
        JsonValue slots = root.get("slots");
        List<SlotData> slotDatas = skeletonJson.addNewSlotDatas(skeletonData, slots);
        List<Slot> newSlots = skeleton.addNewSlots(slotDatas);

        JsonValue skins = root.get("skins");
        skins.remove("attachmentsCount");
        skeletonJson.updateAttachment(skeletonData, skins);
        skeleton.setToSetupPose();

        JsonValue animations = root.get("animations");

        checkAnimBones(animations);

        skeletonJson.updateAnimation(skeletonData, animations);
        AnimationStateData stateData = new AnimationStateData(skeletonData);
        state = new AnimationState(stateData);
        state.setTimeScale(1.0f);

        state.setAnimation(0, "daosuanwu_sender_0", true);
    }

    // TODO 进一步细化
    private void checkAnimBones(JsonValue animations) {
        List<String> animStyle = new ArrayList<>();
        animStyle.add("rotate");
        animStyle.add("translate");
        animStyle.add("scale");
        animStyle.add("shear");
        for (JsonValue slotEntry = animations.child; slotEntry != null; slotEntry = slotEntry.next) {
            JsonValue aniBones = slotEntry.get("bones");
            for (JsonValue bone = aniBones.child; bone != null; bone = bone.next) {
                LogUtil.i(TAG, "Bone name is:" + bone.name);
                for (JsonValue boneChild = bone.child; boneChild != null; boneChild = boneChild.next) {
                    LogUtil.i(TAG, "Bone chlid name is:" + boneChild.name);
                    if (!animStyle.contains(boneChild.name)) {
                        if ("flipX".equals(boneChild.name) || "flipY".equals(boneChild.name)) {
//                            for (int i = 0; i < boneChild.size; i ++) {
//                                if (boneChild.get(i).getBoolean("x")
//                                        || boneChild.get(i).getBoolean("y")) {
//                                    float time = boneChild.get(i).getFloat("time");
//                                    if (i + 1 < boneChild.size) {
//                                        float u = boneChild.get(i + 1).getFloat("time");
//                                    } else {
//                                        float u = time;
//                                    }
//                                }
//                            }
                            bone.remove(boneChild.name);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void render() {
        LogUtil.i(TAG, "Render here.........");
        if (state != null) {
            state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        if (state != null) {
            state.apply(skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
        }
        skeleton.updateWorldTransform(); // Uses the bones' local SRT to compute their world SRT.
        // Configure the camera, SpriteBatch, and SkeletonRendererDebug.
        camera.update();
        batch.getProjectionMatrix().set(camera.combined);
        debugRenderer.getShapeRenderer().setProjectionMatrix(camera.combined);

        batch.begin();
        renderer.draw(batch, skeleton); // Draw the skeleton images.
        batch.end();

//        debugRenderer.draw(skeleton); // Draw debug lines.
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false); // Update camera with new size.
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }

    public void setSkin(String skin) {
        skeleton.setSkin(skin);
    }

    public void setAnimate(String animate) {
        state.addAnimation(0, animate, true, 0); // Jump after 2 seconds.
    }

    public void zoomBig() {
        camera.zoom = 0.5f;
    }

    public void zoomSmall() {
        camera.zoom = 1f;
    }
}

package com.lying.pvz.event.client.handler;

import com.lying.pvz.client.ClientUtil;
import com.lying.pvz.client.model.armor.BucketHeadModel;
import com.lying.pvz.client.model.armor.ConeHeadModel;
import com.lying.pvz.client.model.armor.FootballArmorModel;
import com.lying.pvz.client.model.armor.GigaArmorModel;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jspecify.annotations.Nullable;

public class PvZArmorHandler {

    public static void registerArmorItem(RegisterClientExtensionsEvent event) {



        // =====================================================================
        // BUCKET_HEAD
        // =====================================================================
        event.registerItem(new IClientItemExtensions() {
            private HumanoidModel<?> model;

            @Override
            public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model original) {
                EntityModelSet set = ClientUtil.getMinecraft().getEntityModels();
                if (this.model == null) {
                    this.model = new BucketHeadModel(set.bakeLayer(BucketHeadModel.BUCKET_HEAD_LAYER));
                }
                return this.model;
            }

            @Override
            public @Nullable Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer,
                                                        Identifier _default) {
                return StringUtil.id("textures/models/bucket_head.png");
            }
        }, PvZItemRegister.BUCKET_HEAD.get());

        // =====================================================================
        // CONE_HEAD
        // =====================================================================
        event.registerItem(new IClientItemExtensions() {
            private HumanoidModel<?> model;

            @Override
            public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model original) {
                EntityModelSet set = ClientUtil.getMinecraft().getEntityModels();
                if (this.model == null) {
                    this.model = new ConeHeadModel(set.bakeLayer(ConeHeadModel.CONE_HEAD_LAYER));
                }
                return this.model;
            }

            @Override
            public @Nullable Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer,
                                                        Identifier _default) {
                return StringUtil.id("textures/models/cone_head.png");
            }
        }, PvZItemRegister.CONE_HEAD.get());

        // =====================================================================
        // FOOTBALL_HELMET（模型 + 纹理）
        // =====================================================================
        event.registerItem(new IClientItemExtensions() {
            private HumanoidModel<?> headModel;

            @Override
            public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model original) {
                EntityModelSet set = ClientUtil.getMinecraft().getEntityModels();
                if (this.headModel == null) {
                    this.headModel = new FootballArmorModel(
                            set.bakeLayer(FootballArmorModel.FOOTBALL_HEAD_LAYER), EquipmentSlot.HEAD);
                }
                return this.headModel;
            }

            @Override
            public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer,
                                              Identifier _default) {

                return StringUtil.id("textures/models/football_helmet.png");
            }
        }, PvZItemRegister.FOOTBALL_HELMET.get());

        // =====================================================================
        // FOOTBALL_CHESTPLATE（模型 + 纹理）
        // =====================================================================
        event.registerItem(new IClientItemExtensions() {
            private HumanoidModel<?> chestModel;

            @Override
            public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model original) {
                EntityModelSet set = ClientUtil.getMinecraft().getEntityModels();
                if (this.chestModel == null) {
                    this.chestModel = new FootballArmorModel(
                            set.bakeLayer(FootballArmorModel.FOOTBALL_CHEST_LAYER), EquipmentSlot.CHEST);
                }
                return this.chestModel;
            }

            @Override
            public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer,
                                              Identifier _default) {
                return StringUtil.id("textures/models/football_chestplate.png");
            }
        }, PvZItemRegister.FOOTBALL_CHESTPLATE.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type,
                                              EquipmentClientInfo.Layer layer, Identifier _default) {

                return StringUtil.id("textures/models/armor/football_layer_2.png");
            }
        }, PvZItemRegister.FOOTBALL_LEGGINGS.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type,
                                              EquipmentClientInfo.Layer layer, Identifier _default) {

                return StringUtil.id("textures/models/armor/football_layer_1.png");
            }
        }, PvZItemRegister.FOOTBALL_BOOTS.get());

        // =====================================================================
        // GIGA_HELMET
        // =====================================================================
        event.registerItem(new IClientItemExtensions() {
            private HumanoidModel<?> headModel;

            @Override
            public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model original) {
                EntityModelSet set = ClientUtil.getMinecraft().getEntityModels();
                if (this.headModel == null) {
                    this.headModel = new GigaArmorModel(
                            set.bakeLayer(GigaArmorModel.GIGA_HEAD_LAYER), EquipmentSlot.HEAD);
                }
                return this.headModel;
            }

            @Override
            public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer,
                                              Identifier _default) {
                return StringUtil.id("textures/models/giga_helmet.png");
            }
        }, PvZItemRegister.GIGA_HELMET.get());

        // =====================================================================
        // GIGA_CHESTPLATE
        // =====================================================================
        event.registerItem(new IClientItemExtensions() {
            private HumanoidModel<?> chestModel;

            @Override
            public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model original) {
                EntityModelSet set = ClientUtil.getMinecraft().getEntityModels();
                if (this.chestModel == null) {
                    this.chestModel = new GigaArmorModel(
                            set.bakeLayer(GigaArmorModel.GIGA_CHEST_LAYER), EquipmentSlot.CHEST);
                }
                return this.chestModel;
            }

            @Override
            public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer,
                                              Identifier _default) {
                return StringUtil.id("textures/models/giga_chestplate.png");
            }
        }, PvZItemRegister.GIGA_CHESTPLATE.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type,
                                              EquipmentClientInfo.Layer layer, Identifier _default) {

                return StringUtil.id("textures/models/armor/giga_layer_2.png");
            }
        }, PvZItemRegister.GIGA_LEGGINGS.get());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type,
                                              EquipmentClientInfo.Layer layer, Identifier _default) {

                return StringUtil.id("textures/models/armor/giga_layer_1.png");
            }
        }, PvZItemRegister.GIGA_BOOTS.get());
    }


}

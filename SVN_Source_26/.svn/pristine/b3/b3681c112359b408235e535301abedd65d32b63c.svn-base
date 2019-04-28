package com.app.pao.entity.model;


/**
 * Created by Bowyer on 15/08/06.
 */
public class BottomSheet {


    public enum BottomSheetMenuType {
        SAVE("保存"), DELETE(
                "丢弃"), CANCEL(
                "取消");

        String name;

        BottomSheetMenuType(String name) {
            this.name = name;
        }


        public String getName() {
            return name;
        }
    }

    BottomSheetMenuType bottomSheetMenuType;

    public static BottomSheet to() {
        return new BottomSheet();
    }

    public BottomSheetMenuType getBottomSheetMenuType() {
        return bottomSheetMenuType;
    }

    public BottomSheet setBottomSheetMenuType(BottomSheetMenuType bottomSheetMenuType) {
        this.bottomSheetMenuType = bottomSheetMenuType;
        return this;
    }

}

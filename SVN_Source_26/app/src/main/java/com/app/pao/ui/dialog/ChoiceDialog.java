package com.app.pao.ui.dialog;

import android.support.v4.app.FragmentManager;

import com.app.pao.R;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

/**
 * Created by Raul on 2015/11/12.
 * 带有选择的对话框
 */
public abstract class ChoiceDialog {


    public ChoiceDialog(FragmentManager fm, String title, String msg, String positiveAction, String negativeAction) {
        Dialog.Builder builder = null;
        builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                onPositiveClicked();
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                onNegativeClicked();
                super.onNegativeActionClicked(fragment);
            }
        };
        if (title != null) {
            builder.title(title);
        }
        if (msg != null) {
            ((SimpleDialog.Builder) builder).message(msg);
        }
        builder.positiveAction(positiveAction)
                .negativeAction(negativeAction);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(fm, null);
    }

    public abstract void onPositiveClicked();

    public abstract void onNegativeClicked();

}

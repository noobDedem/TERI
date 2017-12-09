package id.fantasticfive.teri.mahasiswa.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import id.fantasticfive.teri.R;

/**
 * Created by Demas on 11/7/2017.
 */

public class NoticeDialogFragment extends android.support.v4.app.DialogFragment {

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    NoticeDialogListener noticeDialogListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            noticeDialogListener = (NoticeDialogListener) activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + "must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_enrollment, null)).setPositiveButton("CEK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                noticeDialogListener.onDialogPositiveClick(NoticeDialogFragment.this);
            }
        }).setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NoticeDialogFragment.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}

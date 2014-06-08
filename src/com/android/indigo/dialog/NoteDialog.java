package com.android.indigo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.android.indigo.R;
import com.android.indigo.dialog.base.DialogFragmentBase;

public class NoteDialog extends DialogFragmentBase {
	private DialogListener mDialogListener = null;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.dialog_note, null);

		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		lp.height = (int) (metrics.heightPixels * 0.6);
		lp.width = (int) (metrics.widthPixels * 0.9);
		dialog.setContentView(view);
		dialog.getWindow().setLayout(lp.height, lp.width);

		view.findViewById(R.id.notedialog_close_button).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					if (getActivity() instanceof DialogListener) {
						mDialogListener = (DialogListener) getActivity();
						mDialogListener.onClose();
					}
			}
		});
		return dialog;
	}
}

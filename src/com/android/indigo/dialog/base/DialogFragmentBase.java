package com.android.indigo.dialog.base;

import java.util.EventListener;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;

public class DialogFragmentBase extends DialogFragment {
	/*
	 * Set the connection between Dialog and Activity
	 */
	public interface DialogListener extends EventListener {
		public void onClose();
	}
	
	public DialogFragmentBase() {};
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		return dialog;
	}
}

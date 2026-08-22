package com.tungsten.fcl.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.view.LogWindow;
import com.tungsten.fcl.util.ShellUtil;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.view.FCLEditText;

import java.io.File;
import java.util.Objects;

public class ShellActivity extends FCLActivity {

    private EditText logWindow;
    private FCLEditText editText;
    private ShellUtil shellUtil;
    private File shellInit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shell);
        logWindow = findViewById(R.id.shell_log_window);
        editText = findViewById(R.id.shell_input);
        shellInit = new File(FCLPath.INTERNAL_DIR, ".bashrc");
        shellUtil = new ShellUtil(shellInit.getParent(), output -> runOnUiThread(() -> appendLog("\t\t" + output + "\n")));
        shellUtil.start();
        if (shellInit.exists() && shellInit.isFile()) {
            shellUtil.append(". ./.bashrc\n");
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String cmd = Objects.requireNonNull(editText.getText()).toString();
                if (cmd.endsWith("\n")) {
                    appendLog("->" + cmd);
                    editText.setText("");
                    if (cmd.equals("clear\n")) {
                        logWindow.setText("");
                        return;
                    }
                    shellUtil.append(cmd);
                }
            }
        });
        logWindow.setOnClickListener(v -> {
            editText.requestFocus();
        });
        logWindow.setKeyListener(new KeyListener() {
            @Override
            public int getInputType() {
                return 0;
            }

            @Override
            public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
                return true;
            }

            @Override
            public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
                return true;
            }

            @Override
            public boolean onKeyOther(View view, Editable text, KeyEvent event) {
                return true;
            }

            @Override
            public void clearMetaKeyState(View view, Editable content, int states) {

            }
        });
    }

    private void appendLog(String str) {
        logWindow.append(str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shellUtil.interrupt();
    }
}

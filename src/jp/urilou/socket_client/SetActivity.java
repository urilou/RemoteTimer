package jp.urilou.socket_client;

import jp.urilou.socket_client.R.id;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);

		// ê›íËÇÃì«Ç›éÊÇË
		EditText editText = (EditText) findViewById(R.id.server_adress);
		SharedPreferences sp = getSharedPreferences("socket_client",
				MODE_PRIVATE);
		String ip = sp.getString("ipadress", null);
		editText.setText(ip);

		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SetActivity.this, MainActivity.class));
				EditText adresstext = (EditText) findViewById(id.server_adress);
				EditText limtime = (EditText) findViewById(id.limtime);
				SharedPreferences sp = getSharedPreferences("socket_client",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("ipadress", adresstext.getText().toString());
				String strlimtime = limtime.getText().toString();
				if (strlimtime.length() == 0) {
				} else {
					int intlimtime = Integer.parseInt(strlimtime);
					editor.putInt("limtime", intlimtime);
				}

				editor.commit();
			}
		});
	}
}

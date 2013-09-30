package jp.urilou.socket_client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MainActivity extends Activity {
	Timer timer;
	Handler handler = new Handler();
	boolean timerenable = false;
	int intsec = 0;
	int intmin = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// �^�C�}�[�Ŏg���t�H���g�̐ݒ�
		Typeface face = Typeface.createFromAsset(getAssets(), "migmix.ttf");
		Button timebt = (Button) findViewById(R.id.timer);
		timebt.setText("0:00");
		timebt.setTypeface(face);

		// �o�b�N�O���E���h�ŋ����I�ɓ��삳����
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.permitAll().build());

		// �ݒ�f�[�^�̓ǂݎ��
		SharedPreferences sp = getSharedPreferences("socket_client",
				MODE_PRIVATE);
		String ipadress = sp.getString("ipadress", "err");
		ipadress = ipadress.replaceAll("#", ".");
		int intlimtime = sp.getInt("limtime", 32767);
		final int fnlimtime = intlimtime;
		final String address = ipadress;
		final int port = 10010;
		Toast.makeText(this, "�R���s���[�^�[��IP�A�h���X: \n" + ipadress,
				Toast.LENGTH_SHORT).show();

		// �^�C�}�[
		View timerbt = findViewById(R.id.timer);
		// �^�C�}�[�Z����
		timerbt.setOnClickListener(new OnClickListener() {
			Button bt = (Button) findViewById(R.id.timer);
			private long pattern[] = { 0, 100, 100, 100 };

			@Override
			public void onClick(View v) {
				// �^�C�}�[�������Ă���Ƃ�
				if (timerenable) {
					// �X���[�v�֎~����
					getWindow().clearFlags(
							WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					// �^�C�}�[��~
					timerenable = false;
					timer.cancel();
					timer = null;
				} else {
					// �^�C�}�[���~�܂��Ă���Ƃ�
					// �X���[�v�֎~
					getWindow().addFlags(
							WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
					// �^�C�}�[
					timerenable = true;
					timer = new Timer(true);
					timer.scheduleAtFixedRate(new TimerTask() {
						public void run() {
							handler.post(new Runnable() {
								public void run() {
									intsec = intsec + 1;
									if (intsec == 60) {
										intmin = intmin + 1;
										intsec = 0;
									}
									String strSec = String.valueOf(intsec);
									String strMin = String.valueOf(intmin);
									if (intsec == 0) {
										strSec = "00";
									} else if (intsec == 1) {
										strSec = "01";
									} else if (intsec == 2) {
										strSec = "02";
									} else if (intsec == 3) {
										strSec = "03";
									} else if (intsec == 4) {
										strSec = "04";
									} else if (intsec == 5) {
										strSec = "05";
									} else if (intsec == 6) {
										strSec = "06";
									} else if (intsec == 7) {
										strSec = "07";
									} else if (intsec == 8) {
										strSec = "08";
									} else if (intsec == 9) {
										strSec = "09";
									}
									Log.v("time", strSec);
									bt.setText(strMin + ":" + strSec);

									if (((intmin + 1) == fnlimtime)
											&& (intsec == 1)) {
										// �����F�ύX
										bt.setTextColor(Color.YELLOW);
										// �U��
										Vibrator vib;
										vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
										vib.vibrate(50);
									}

									if ((intmin == fnlimtime) && (intsec == 0)) {
										// �U��
										Vibrator vib;
										vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
										vib.vibrate(pattern, -1);
										bt.setTextColor(Color.RED);
									}
								}
							});
						}
					}, 0, 1000);
				}
			}
		});

		// �^�C�}�[������
		timerbt.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				Button timerbtn = (Button) findViewById(R.id.timer);
				// �^�C�}�[�\���̏�����
				timerbtn.setText("0:00");
				timerbtn.setTextColor(Color.WHITE);
				// �X���[�v�֎~�̉���
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				// �U��
				Vibrator vib;
				vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
				vib.vibrate(50);
				// �^�C�}�[���쎞
				if (timerenable) {
					// �^�C�}�[���Z�b�g
					timerenable = false;
					timer.cancel();
					timer = null;
					intsec = 0;
					intmin = 0;
				} else {
					// �^�C�}�[��~��
					timerenable = false;
					timer = null;
					intsec = 0;
					intmin = 0;
				}
				return true;
			}
		});

		// �߂�{�^��
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Socket socket = null;
				try {
					socket = new Socket(address, port);
					PrintWriter pw = new PrintWriter(socket.getOutputStream(),
							true);
					String sendTxt = "powerpoint.page.back";
					pw.println(sendTxt);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (socket != null) {
					try {
						socket.close();
						socket = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// �v���[���{�^��
		View startbt = findViewById(R.id.start);
		// �v���[���{�^���Z����
		startbt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Socket socket = null;
				try {
					socket = new Socket(address, port);
					PrintWriter pw = new PrintWriter(socket.getOutputStream(),
							true);
					String sendTxt = "powerpoint.app.current";
					pw.println(sendTxt);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (socket != null) {
					try {
						socket.close();
						socket = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// �v���[���{�^��������
		startbt.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				Socket socket = null;
				try {
					socket = new Socket(address, port);
					PrintWriter pw = new PrintWriter(socket.getOutputStream(),
							true);
					String sendTxt = "powerpoint.app.first";
					pw.println(sendTxt);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (socket != null) {
					try {
						socket.close();
						socket = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return false;
			}
		});

		// ���փ{�^��
		findViewById(R.id.next).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Socket socket = null;
				try {
					socket = new Socket(address, port);
					PrintWriter pw = new PrintWriter(socket.getOutputStream(),
							true);
					String sendTxt = "powerpoint.page.next";
					pw.println(sendTxt);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (socket != null) {
					try {
						socket.close();
						socket = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// �E�B���h�E�̐؂�ւ��{�^��
		findViewById(R.id.swtch).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Socket socket = null;
				try {
					socket = new Socket(address, port);
					PrintWriter pw = new PrintWriter(socket.getOutputStream(),
							true);
					String sendTxt = "windows.app.switch";
					pw.println(sendTxt);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (socket != null) {
					try {
						socket.close();
						socket = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// ����{�^��
		findViewById(R.id.close).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Socket socket = null;
				try {
					socket = new Socket(address, port);
					PrintWriter pw = new PrintWriter(socket.getOutputStream(),
							true);
					String sendTxt = "windows.app.close";
					pw.println(sendTxt);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (socket != null) {
					try {
						socket.close();
						socket = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// �e�X�g�{�^��
		findViewById(R.id.test).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Vibrator vib;
				vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
				vib.vibrate(50);
				Socket socket = null;
				try {
					socket = new Socket(address, port);
					PrintWriter pw = new PrintWriter(socket.getOutputStream(),
							true);
					String sendTxt = "test";
					pw.println(sendTxt);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (socket != null) {
					try {
						socket.close();
						socket = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// ���L�{�^��
		findViewById(R.id.share).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder
						.from(MainActivity.this);
				// 0-9�̃����_���Ȑ����o��
				Random rnd = new Random();
				String rantext = null;
				int ran = rnd.nextInt(7);
				if (ran == 0) {
					rantext = "�ƂĂ��킩��₷���ł��I ";
				}
				if (ran == 1) {
					rantext = "�Ƒn�I�ł����I ";
				}
				if (ran == 2) {
					rantext = "�C���p�N�g������܂����I ";
				}
				if (ran == 3) {
					rantext = "�f���炵�����e�ł����I ";
				}
				if (ran == 4) {
					rantext = "�������܂����I ";
				}
				if (ran == 5) {
					rantext = "���Ԃ�Y��Ă��܂��܂����I ";
				}
				if (ran == 6) {
					rantext = "���ʂ�����܂���I ";
				}
				// �^�C�}�[�{�^���̕�����ǂݎ��
				Button btn = (Button) findViewById(R.id.timer);
				String timertext = (String) btn.getText();
				// ���L���镶����
				builder.setText(rantext + "����̃v���[�����Ԃ�" + timertext + "�ł�");
				builder.setType("text/plain");
				builder.startChooser();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

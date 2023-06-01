package com.example.translatelg;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.ArrayList;
import java.util.Locale;


public class HomeFragment extends Fragment {
    private Spinner fromSpinner, toSpinner;
    private TextInputEditText textInput;
    private ImageView micIV, idCopy, idSound, idCamera, idCopyRS, idSoundRS, share;
    private MaterialButton translateBtn;
    private TextView translatedTV;
    private TextToSpeech voice;
    String[] fromLanguages = {
            "From", "English", "Spanish", "French", "German", "Italian", "Portuguese", "Russian", "Chinese (Simplified)", "Japanese", "Korean", "Vietnamese"
    };
    String[] toLanguages = {
            "To", "English", "Spanish", "French", "German", "Italian", "Portuguese", "Russian", "Chinese (Simplified)", "Japanese", "Korean", "Vietnamese"
    };
    private static final int REQUES_PERMISSON_CODE = 1;
    private int fromLanguageCode, toLanguageCode = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // lấy danh sách ngôn ngữ
        fromSpinner = view.findViewById(R.id.idFromSpinder);
        toSpinner = view.findViewById(R.id.idToSpinder);
        //đầu vào văn bản
        textInput = view.findViewById(R.id.idEdtSource);
        //giọng nói > văn bản
        micIV = view.findViewById(R.id.idIVMic);
        translateBtn = view.findViewById(R.id.idBtnTraslate);
        //hiển thị kq
        translatedTV = view.findViewById(R.id.idTVTranslateTV);
        //sao chép, tạo sự kiện trở trang layout
        idCopy = view.findViewById(R.id.idCopy);
        //chuyển văn bản thành giọng nói
        idSound = view.findViewById(R.id.idSound);
        //camera
        idCamera = view.findViewById(R.id.idCamera);
        //sao chép tạo sự kiện trở trang layout
        idCopyRS = view.findViewById(R.id.idCopyRS);
        idSoundRS = view.findViewById(R.id.idSoundRS);
        share = view.findViewById(R.id.share);

        // Kiểm tra quyền của máy ảnh
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//kiểm tra xem phiên bản SDK của hệ điều hành Android có lớn hơn hoặc bằng phiên bản 6.0 (M) hay không
            if (getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {//kiểm tra xem ứng dụng đã được cấp quyền sử dụng camera chưa
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);//hàm dùng để yêu cầu cấp quyền, mã yêu cầu là 101
            }
        }

        // bắt sự kiện nút máy ảnh
        idCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//yêu cầu hệ thống khởi máy ảnh
                startActivityForResult(intent, 101);//khởi động Intent và đợi kết quả trả về từ ứng dụng máy ảnh
            }
        });


        //  chuyển văn bản thành giọng nói
        voice = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {//khởi tạo TextToSpeech và cấu hình chuyển đổi văn bản thành giọng nói
            @Override
            public void onInit(int status) {//gọi phươn thức này khi TextToSpeech xảy ra
                if (status != TextToSpeech.ERROR) {// kiểm tra thành công ko
                    voice.setLanguage(Locale.ENGLISH);//TextToSpeech được đặt là Locale.ENGLISH
                }
            }
        });
        // giọng nói > văn bản
        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);// sử dụng  một hoạt động nhận dạng giọng nói
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);//thiết lập ngôn ngữ sử dụng  nhận dạng giọng nói một cách tự do
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());//sử dụng ngôn ngữ được thiết lập trên thiết bị để thực hiện nhiệm vụ nhận dạng giọng nói
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Nói gì đó..."); // thông báo cho người dùng trước khi họ bắt đầu nói
                try {
                    startActivityForResult(i, REQUES_PERMISSON_CODE);//yêu cầu quyền và chạy nhận dạng giọng nói
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();// báo lỗi
                }
            }
        });

        // phát âm thanh đoạn văn bản
        //được sử dụng để xử lý sự kiện click trên một button có id
        idSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // biến text gán bằng nội dung văn bản từ giao diện thông qua
                String text = textInput.getText().toString();
                voice.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                // phát âm giọng nói speak(), text nd văn bản, phát âm ko bị lồng nhau,theo dõi quá trình phát âm

            }
        });

        // phát âm thanh đoạn văn bản
        idSoundRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = translatedTV.getText().toString();
                voice.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });


        // copy văn bản
        idCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  khởi tạo bằng cách gọi phương thức từ require, clipboarmanager dc sử dụng để quản lý nội dung bộ nhớ tạm
                // // đối tượng dc khởi tạo bằng cách gọi phương thức từ require, clipboarmanager dc sử dụng để quan lý nội dung bộ nhớ tạm
                ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                //một đối tượng ClipData mới được tạo bằng cách gọi phương thức newPlainText
                ClipData clipData = ClipData.newPlainText("Sao chép", textInput.getText().toString());
                // Đối tượng ClipData này chứa dữ liệu văn bản cần sao chép
                clipboardManager.setPrimaryClip(clipData);
                //văn bản được sao chép từ textInput sẽ được đặt vào clipboard để có thể dán vào các ứng dụng khác.
                Toast.makeText(getContext(), "Đã sao chép", Toast.LENGTH_SHORT).show();
                // tb sao chép thành công
            }
        });


        // copy văn bản sau khi dịch

        idCopyRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Sao chép", translatedTV.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(), "Đã sao chép", Toast.LENGTH_SHORT).show();
            }
        });
        //share
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // tạo đt intent thực hiện hoạt động chia sẻ dl ,..
                Intent intent = new Intent();
                // xác định hđ là gửi dl
                intent.setAction(Intent.ACTION_SEND);
                // đặt dl là dòng vb
                intent.setType("text/plain");
                // gắn thêm dl vào itent, nội dung văn bản từ textview
                intent.putExtra(Intent.EXTRA_TEXT, translatedTV.getText().toString());
                // chạy hd androi chọn ứng dụng chia sẻ , với tiêu đề
                startActivity(Intent.createChooser(intent, "Chia sẻ qua"));
            }
        });
        // hiển thị danh sách ngôn ngữ
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
                fromLanguageCode = getLanguageCode(fromLanguages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //hiển thị vào khung
        ArrayAdapter fromAdapter = new ArrayAdapter(requireContext(), R.layout.spinner_item, fromLanguages);// tạo mới adpter mục trong spiner
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // hiển thị mục thả xuống của Spinner.
        fromSpinner.setAdapter(fromAdapter);
        //hiển thị danh sách ngôn ngữ cần dịch
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguageCode = getLanguageCode(toLanguages[i]); //lấy ngôn ngữ
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // hiển thị danh sách ngôn ngữ
        ArrayAdapter toAdapter = new ArrayAdapter(requireContext(), R.layout.spinner_item, toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);
        //sự kiện nhấn nút dịch
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translatedTV.setText("");
                if (textInput.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
                } else if (fromLanguageCode == 0) {
                    Toast.makeText(requireContext(), "Vui lòng chọn ngôn ngữ", Toast.LENGTH_SHORT).show();
                } else if (toLanguageCode == 0) {
                    Toast.makeText(requireContext(), "Vui lòng chọn ngôn ngữ cần dịch", Toast.LENGTH_SHORT).show();
                } else {
                    idCopyRS.setVisibility(View.VISIBLE);
                    idSoundRS.setVisibility(View.VISIBLE);
                    translatedTV.setVisibility(View.VISIBLE);
                    share.setVisibility(View.VISIBLE);
                    transientText(fromLanguageCode, toLanguageCode, textInput.getText().toString());
                }
            }
        });

        return view;
    }
    //nhận dạng văn bản trên một hình ảnh bằng cách sử dụng Firebase ML Kit.
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUES_PERMISSON_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);//lấy danh sách các kết quả từ hoạt động nhận dạng giọng nói
                textInput.setText(result.get(0));
            }
        } else if (requestCode == 101 && resultCode == RESULT_OK) {//kiểm tra kết quả trả về của một hoạt động yêu cầu chụp ảnh
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap bitmap = (Bitmap) bundle.get("data");//sử dụng đối tượng Bundle để lấy dữ liệu trả về và chuyển đổi ảnh thành đối tượng Bitmap
                if (bitmap != null) {
                    FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap); //tạo FirebaseVisionImage từ bitmap
                    FirebaseVision firebaseVision = FirebaseVision.getInstance();
                    FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();//sử dụng OnDeviceTextRecognizer từ FirebaseVision để xử lý ảnh và trích xuất văn bản trong ảnh đó
                    Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);//tạo task xử lý hình ảnh và nhận dạng văn bản trong đó
                    //Nếu tác vụ trong task thành công, listener sẽ được gọi onSuccess và xử lý kết quả
                    task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {

                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            String text = firebaseVisionText.getText();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textInput.setText(text);
                                } //cập nhật văn bản
                            });
                        }
                    });
                    // Nếu tác vụ trong task thất bại, listener sẽ được gọi onFailure và hiển thị một thông báo lỗi trên màn hình
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();//hiển thị một thông báo lỗi trên màn hình
                        }
                    });
                }
            }
        }
    }

    private void transientText(int fromLanguageCode, int toLanguageCode, String text) {
        translatedTV.setText("Đang dịch ...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()// cấu hình
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();
        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options); //Cấu hình
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();// tải xuống ngôn ngữ
        translator.downloadModelIfNeeded(conditions)// tiến hành tải
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        translatedTV.setText("Đang dịch...");
                        translator.translate(text) //gọi pt của đối tượng translator
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String translated) {
                                        translatedTV.setText(translated);
                                        DBHelper dbHelper = new DBHelper(getContext());
                                        // Thêm bản dịch vào cơ sở dữ liệu
                                        dbHelper.insertTranslation(text, translated);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Dịch thất bại" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Tải xuống ngôn ngữ thất bại" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // chọn ngôn ngữ
    public int getLanguageCode(String language) {
        int languageCode = 0;
        switch (language) {
            case "Afrikaans":
                languageCode = FirebaseTranslateLanguage.AF;
                break;
            case "English":
                languageCode = FirebaseTranslateLanguage.EN;
                break;
            case "Spanish":
                languageCode = FirebaseTranslateLanguage.ES;
                break;
            case "French":
                languageCode = FirebaseTranslateLanguage.FR;
                break;
            case "German":
                languageCode = FirebaseTranslateLanguage.DE;
                break;
            case "Italian":
                languageCode = FirebaseTranslateLanguage.IT;
                break;
            case "Portuguese":
                languageCode = FirebaseTranslateLanguage.PT;
                break;
            case "Russian":
                languageCode = FirebaseTranslateLanguage.RU;
                break;
            case "Chinese (Simplified)":
                languageCode = FirebaseTranslateLanguage.ZH;
                break;
            case "Japanese":
                languageCode = FirebaseTranslateLanguage.JA;
                break;
            case "Korean":
                languageCode = FirebaseTranslateLanguage.KO;
                break;
            case "Vietnamese":
                languageCode = FirebaseTranslateLanguage.VI;
                break;
            default:
                languageCode = 0;
        }
        return languageCode;
    }


}
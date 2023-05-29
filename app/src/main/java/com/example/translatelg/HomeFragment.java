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
    private ImageView micIV, idCopy, idSound, idCamera, idCopyRS, idSoundRS,share;
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

    private ArrayList<Pair<String, String>> translationHistory = new ArrayList<>();



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
        //sao chép
        idCopy = view.findViewById(R.id.idCopy);
        //chuyển văn bản thành giọng nói
        idSound = view.findViewById(R.id.idSound);
        //camera
        idCamera = view.findViewById(R.id.idCamera);
        //sao chép
        idCopyRS = view.findViewById(R.id.idCopyRS);
        idSoundRS = view.findViewById(R.id.idSoundRS);
        share = view.findViewById(R.id.share);

        // Check camera permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
            }
        }

        // Camera button click listener
        idCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 101);
            }
        });

        // Text-to-speech initialization
        voice = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    voice.setLanguage(Locale.ENGLISH);
                }
            }
        });
        // giọng nói > văn bản
        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Nói gì đó...");
                try {
                    startActivityForResult(i, REQUES_PERMISSON_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Speak button click listener
        idSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = textInput.getText().toString();
                voice.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        // Speak translated button click listener
        idSoundRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = translatedTV.getText().toString();
                voice.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        // Copy button click listener
        idCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Sao chép", textInput.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(), "Đã sao chép", Toast.LENGTH_SHORT).show();
            }
        });

        // Copy translated button click listener
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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, translatedTV.getText().toString() );
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
        ArrayAdapter fromAdapter = new ArrayAdapter(requireContext(), R.layout.spinner_item, fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);
        //hiển thị danh sách ngôn ngữ cần dịch
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguageCode = getLanguageCode(toLanguages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // hiển thị danh sách ngôn ngữ
        ArrayAdapter toAdapter = new ArrayAdapter(requireContext(), R.layout.spinner_item, toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUES_PERMISSON_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                textInput.setText(result.get(0));
            }
        } else if (requestCode == 101 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap bitmap = (Bitmap) bundle.get("data");
                if (bitmap != null) {
                    FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
                    FirebaseVision firebaseVision = FirebaseVision.getInstance();
                    FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
                    Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
                    task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            String text = firebaseVisionText.getText();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textInput.setText(text);
                                }
                            });
                        }
                    });
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    private void transientText(int fromLanguageCode, int toLanguageCode, String text) {
        translatedTV.setText("Đang dịch ...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();
        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        translatedTV.setText("Đang dịch...");
                        translator.translate(text)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String translated) {
                                        translatedTV.setText(translated);
                                        DBHelper dbHelper = new DBHelper(getContext());
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

    private String getLanguageFromCode(int languageCode) {
        String language = "";
        switch (languageCode) {
            case FirebaseTranslateLanguage.AF:
                language = "Afrikaans";
                break;
            case FirebaseTranslateLanguage.EN:
                language = "English";
                break;
            case FirebaseTranslateLanguage.ES:
                language = "Spanish";
                break;
            case FirebaseTranslateLanguage.FR:
                language = "French";
                break;
            // Thêm các case cho các ngôn ngữ khác
        }
        return language;
    }


}
package doh.nvbsp.nbbnets.donorverifier;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

public class ManualOnLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_on_login);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        PDFView pdfView= findViewById(R.id.pdfView);
        pdfView.fromAsset("verifier_manual.pdf")
//                .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
        .pages(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18) // all pages are displayed by default
        .enableSwipe(true) // allows to block changing pages using swipe
        .swipeHorizontal(false)
        .enableDoubletap(true)
        .defaultPage(0)
        .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
        .password(null)
        .scrollHandle(null)
        .enableAntialiasing(true) // improve rendering a little bit on low-res screens
        // spacing between pages in dp. To define spacing color, set view background
        .spacing(0)
        .pageFitPolicy(FitPolicy.WIDTH)
        .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
        .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
        .pageSnap(false) // snap pages to screen boundaries
        .pageFling(false) // make a fling change only a single page like ViewPager
        .nightMode(false) // toggle night mode
        .load();


    }
}

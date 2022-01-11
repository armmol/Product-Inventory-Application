package com.example.product_inventory_application;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class activity_additem extends AppCompatActivity {

    private TextView Name, Description, Weight;
    private Button btn;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_additem);

        btn = findViewById (R.id.btn_additemmanually);


        Name = findViewById (R.id.edtxt_name);
        Description = findViewById (R.id.edtxt_description);
        Weight = findViewById (R.id.edtxt_weight);

        btn.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {

                String name = Name.getText ().toString ();
                String description = Description.getText ().toString ();
                double weight = 0;

                try {

                    weight = Double.parseDouble (Weight.getText ().toString ());
                } catch (Error e) {

                }
                Connection connection = SQLConnection.CONN ();

                PreparedStatement pst = null;

                if (connection != null) {
                    Statement statement = null;
                    try {
                        // Creates a statement that is is sent to the server.
                        statement = connection.createStatement ();
                        pst = connection.prepareStatement ("INSERT INTO WarehouseItem ([WarehouseID],[Name],[Description],[QRCode],[Weight],[Height],[Lenght],[Width],[IMAGE]) VALUES (?,?,?,?,?,?,?,?,?)");
                        pst.setInt (1, 1);
                        pst.setString (2, name);
                        pst.setString (3, description);
                        pst.setString (4, null);
                        pst.setDouble (5, weight);
                        pst.setDouble (6, 0);
                        pst.setDouble (7, 0);
                        pst.setDouble (8, 0);
                        pst.setBytes (9, null);
                        //pst.setInt (10, 1);

                        pst.executeUpdate();

                        Toast.makeText (activity_additem.this, "Successfully added an item", Toast.LENGTH_SHORT).show ();


                    } catch (SQLException e) {
                        e.printStackTrace ();
                    }
                }else
                    Toast.makeText (activity_additem.this, "Unsuccessful", Toast.LENGTH_SHORT).show ();
            }
        });
    }

}

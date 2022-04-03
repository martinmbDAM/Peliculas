package com.example.peliculas;

import androidx.appcompat.app.AppCompatActivity;
import com.sparsity.sparksee.gdb.*;

import android.os.Bundle;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private SparkseeConfig cfg;
    private Database db;
    private Sparksee sparksee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cfg = new SparkseeConfig(getApplicationContext(),"sparksee.cfg");
        cfg.setClientId("0c00c718-6d72-45b5-a5b6-bb77ec2f6623");
        cfg.setLicenseId("77495f76-198e-4787-b1c9-d35a7a3cd564");
        sparksee = new Sparksee(cfg);
        try {
            db = sparksee.create("HelloSparksee.gdb", "HelloSparksee");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.runMyApp();
    }

    private void runMyApp(){

        Session sess = db.newSession();
        Graph g = sess.getGraph();

        // Add a node type for the movies , with a unique identifier and two indexed attributes
        int movieType = g.newNodeType("MOVIE");
        int movieIdType = g.newAttribute(movieType , "ID", DataType.Long, AttributeKind.
                Unique);
        int movieTitleType = g.newAttribute(movieType , "TITLE", DataType.String,
                AttributeKind.Indexed);
        int movieYearType = g.newAttribute(movieType , "YEAR", DataType.Integer ,
                AttributeKind.Indexed);
        // Add a node type for the people , with a unique identifier and an indexed attribute
        int peopleType = g.newNodeType("PEOPLE");
        int peopleIdType = g.newAttribute(peopleType , "ID", DataType.Long, AttributeKind.
                Unique);
        int peopleNameType = g.newAttribute(peopleType , "NAME", DataType.String,
                AttributeKind.Indexed);

        // Add an undirected edge type with an attribute for the cast of a movie
        int castType = g.newEdgeType("CAST", false , false);
        int castCharacterType = g.newAttribute(castType , "CHARACTER", DataType.String,
                AttributeKind.Basic);
        // Add a directed edge type restricted to go from people to movie for the director of a movie
        int directsType = g.newRestrictedEdgeType("DIRECTS", peopleType , movieType , false)
                ;

        // Add some MOVIE nodes
        Value value = new Value();
        long mLostInTranslation = g.newNode(movieType);
        g.setAttribute(mLostInTranslation , movieIdType , value.setLong(1));
        g.setAttribute(mLostInTranslation , movieTitleType , value.setString("Lost in Translation"));
                g.setAttribute(mLostInTranslation , movieYearType , value.setInteger(2003));
        long mVickyCB = g.newNode(movieType);
        g.setAttribute(mVickyCB , movieIdType , value.setLong(2));
        g.setAttribute(mVickyCB , movieTitleType , value.setString("Vicky Cristina Barcelona"));
                g.setAttribute(mVickyCB , movieYearType , value.setInteger(2008));
        long mManhattan = g.newNode(movieType);
        g.setAttribute(mManhattan , movieIdType , value.setLong(3));
        g.setAttribute(mManhattan , movieTitleType , value.setString("Manhattan"));

        g.setAttribute(mManhattan , movieYearType , value.setInteger(1979));
        // Add some PEOPLE nodes
        long pScarlett = g.newNode(peopleType);
        g.setAttribute(pScarlett , peopleIdType , value.setLong(1));
        g.setAttribute(pScarlett , peopleNameType , value.setString("Scarlett Johansson"));
        long pBill = g.newNode(peopleType);
        g.setAttribute(pBill, peopleIdType , value.setLong(2));
        g.setAttribute(pBill, peopleNameType , value.setString("Bill Murray"));
        long pSofia = g.newNode(peopleType);
        g.setAttribute(pSofia, peopleIdType , value.setLong(3));
        g.setAttribute(pSofia, peopleNameType , value.setString("Sofia Coppola"));
        long pWoody = g.newNode(peopleType);
        g.setAttribute(pWoody, peopleIdType , value.setLong(4));
        g.setAttribute(pWoody, peopleNameType , value.setString("Woody Allen"));
        long pPenelope = g.newNode(peopleType);
        g.setAttribute(pPenelope , peopleIdType , value.setLong(5));
        g.setAttribute(pPenelope , peopleNameType , value.setString("Penélope Cruz"));
        long pDiane = g.newNode(peopleType);
        g.setAttribute(pDiane, peopleIdType , value.setLong(6));
        g.setAttribute(pDiane, peopleNameType , value.setString("Diane Keaton"));

        // Add some CAST edges
        // Remember that we are reusing the Value class instance to set the attributes
        long anEdge;
        anEdge = g.newEdge(castType , mLostInTranslation , pScarlett);
        g.setAttribute(anEdge, castCharacterType , value.setString("Charlotte"));
        anEdge = g.newEdge(castType , mLostInTranslation , pBill);
        g.setAttribute(anEdge, castCharacterType , value.setString("Bob Harris"));
        anEdge = g.newEdge(castType , mVickyCB , pScarlett);
        g.setAttribute(anEdge, castCharacterType , value.setString("Cristina"));
        anEdge = g.newEdge(castType , mVickyCB , pPenelope);
        g.setAttribute(anEdge, castCharacterType , value.setString("Maria Elena"));
        anEdge = g.newEdge(castType , mManhattan , pDiane);
        g.setAttribute(anEdge, castCharacterType , value.setString("Mary"));
        anEdge = g.newEdge(castType , mManhattan , pWoody);
        g.setAttribute(anEdge, castCharacterType , value.setString("Isaac"));
        // Add some DIRECTS edges
        anEdge = g.newEdge(directsType , pSofia, mLostInTranslation);
        anEdge = g.newEdge(directsType , pWoody, mVickyCB);
        anEdge = g.newEdge(directsType , pWoody, mManhattan);
    }
}
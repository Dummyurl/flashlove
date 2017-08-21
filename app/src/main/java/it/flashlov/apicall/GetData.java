package it.flashlov.apicall;

/**
 * Created by Karshima on 1/16/2017.
 */




    import android.os.AsyncTask;
    import org.apache.http.NameValuePair;
    import org.json.JSONObject;

    import java.util.List;


    public class GetData extends AsyncTask<String, Void, Boolean> {

        private JSONParser jParser = new JSONParser();
        ResultListner resultListener;
        int  status;
        private JSONObject response;
        List<NameValuePair> params;


        public GetData(List<NameValuePair> params){

            this.params=params;
        }

        public void setResultListener(ResultListner listener) {
            this.resultListener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            String URL=urls[0];

            try {
                // getting JSON string from URL
                JSONObject json = jParser.makeHttpRequest(URL, "POST", params);
                response=json;
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        protected void onPostExecute(Boolean result) {

            if(result){

                resultListener.onSuccess(response);
            }else {

                resultListener.onFailed();

            }

        }

        public static interface ResultListner{

            void onSuccess(JSONObject json);
            void onFailed();
        }

    }



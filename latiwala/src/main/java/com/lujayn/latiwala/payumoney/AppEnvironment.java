package com.lujayn.latiwala.payumoney;


/**
 * Created by Raju vaghela on 07/09/18.
 */
public enum AppEnvironment {
    SANDBOX {
        @Override
        public String merchant_Key() {
            return "6wSAmsUT";
        }

        @Override
        public String merchant_ID() {
            return "6437487";
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "EeGJpaKf2F";
        }

        /**
         * Testing crede.
         * @return
         */
      /*  @Override
        public String merchant_Key() {
            return "LLKwG0";
        }

        @Override
        public String merchant_ID() {
            return "393463";
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "qauKbEAJ";
        }*/

        @Override
        public boolean debug() {
            return true;
        }
    },
    PRODUCTION {
        @Override
        public String merchant_Key() {
            return "6wSAmsUT";
        }  //O15vkB

        @Override
        public String merchant_ID() {
            return "6437487";
        }   //4819816

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "EeGJpaKf2F";
        }     //LU1EhObh

        @Override
        public boolean debug() {
            return false;
        }
    };

    public abstract String merchant_Key();

    public abstract String merchant_ID();

    public abstract String furl();

    public abstract String surl();

    public abstract String salt();

    public abstract boolean debug();


}

package flipkart.pricing.apps.kaizen.db.domain;

public enum SignalDataTypes {

    INT {
        @Override
        public boolean isValid(String value) {
            if (value==null){
                return true;
            }
            try {
                Integer.parseInt(value);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }
    },
    DOUBLE {
        @Override
        public boolean isValid(String value) {
            if (value==null){
                return true;
            }
            try {
                Double.parseDouble(value);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }
    },
    FLOAT {
        @Override
        public boolean isValid(String value) {
            if (value==null){
                return true;
            }
            try {
                Float.parseFloat(value);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }
    },
    LONG {
        @Override
        public boolean isValid(String value) {
            if (value==null){
                return true;
            }
            try {
                Long.parseLong(value);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }
    },
    STRING {
        @Override
        public boolean isValid(String value) {
            return true;
        }
    };

    abstract public boolean isValid(String value);


}

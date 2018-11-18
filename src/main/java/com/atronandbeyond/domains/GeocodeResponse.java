package com.atronandbeyond.domains;

import java.util.List;

public class GeocodeResponse {
    public class Results {
        public class AddressComponents {
            private String long_name;
            private String short_name;
            private List<String> types;

            public String getLong_name() {
                return long_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public List<String> getTypes() {
                return types;
            }

            @Override
            public String toString() {
                return "AddressComponents{" +
                        "long_name='" + long_name + '\'' +
                        ", short_name='" + short_name + '\'' +
                        ", types=" + types +
                        '}';
            }
        }
        private List<AddressComponents> address_components;

        public List<AddressComponents> getAddress_components() {
            return address_components;
        }

        @Override
        public String toString() {
            return "Results{" +
                    "address_components=" + address_components +
                    '}';
        }
    }
    private List<Results> results;

    public List<Results> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "GeocodeResponse{" +
                "results=" + results +
                '}';
    }
}

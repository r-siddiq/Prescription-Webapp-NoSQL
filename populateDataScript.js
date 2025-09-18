// Dropping collections to have blank collections
db.drug.drop();
db.pharmacy.drop();

// Data for Drugs
const drugData = [
    { _id: 1, name: "loratadine"},
    { _id: 2, name: "acetaminophen"},
    { _id: 3, name: "lisinopril"},
    { _id: 4, name: "lovastatin"}
];

// Adding data to drug collection
db.drug.insertMany(drugData);

// Data for Pharmacies
const pharmacyDate = [
    { _id: 1, name: "CVS", address: "123 Main St.", phone: "123-456-7890",
        drugCosts: [
            { drugName: 'loratadine', cost: 7.5},
            { drugName: 'acetaminophen', cost: 3.5},
            { drugName: 'lisinopril', cost: 1.5},
            { drugName: 'lovastatin', cost: 2.5}
        ]
    },
    { _id: 2, name: "RightAid", address: "321 Left St.", phone: "908-765-4321",
        drugCosts: [
            { drugName: 'loratadine', cost: 2.5},
            { drugName: 'acetaminophen', cost: 6.5},
            { drugName: 'lisinopril', cost: 3.5},
            { drugName: 'lovastatin', cost: 1.5}
        ]},
    { _id: 3, name: "Bullseye", address: "421 Right St.", phone: "111-222-3333",
        drugCosts: [
            { drugName: 'loratadine', cost: 1.5},
            { drugName: 'acetaminophen', cost: 2.5},
            { drugName: 'lisinopril', cost: 8.5},
            { drugName: 'lovastatin', cost: 4.5}
        ]}
];

// Adding data to Pharmacy collection
db.pharmacy.insertMany(pharmacyDate);

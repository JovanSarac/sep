INSERT INTO public.services (
    id, name, description, monthly_fee, is_active
) 
VALUES 
    (-1, 
    'Bank Payment Service (Card)', 
    'A comprehensive payment solution that allows users to make online transactions using credit and debit cards. This service offers high levels of security and encryption, ensuring safe and reliable payments for users and merchants alike. It supports multiple card types and can be integrated with most major banking institutions.', 
    99.99, 
    TRUE
    ),
    (-2, 
    'Bank Payment Service (QR Code)', 
    'This payment service facilitates transactions through QR codes. Users can easily pay by scanning the provided QR code using their mobile banking apps or QR scanners. The service is highly convenient and ideal for in-store and online payments, providing fast and secure transactions without needing physical cards.', 
    129.99, 
    TRUE
    ),
    (-3, 
    'PayPal Payment Service', 
    'PayPal is one of the worlds leading online payment platforms, allowing users to make payments and money transfers through their PayPal account. It supports a variety of payment methods including linked credit/debit cards and bank accounts. The service ensures secure transactions and is widely accepted by online merchants globally.', 
    59.99, 
    TRUE
    ),
    (-4, 
    'Cryptocurrency Payment Service (Bitcoin)', 
    'This payment service enables users to make payments using cryptocurrency, with Bitcoin as the primary supported currency. Transactions are processed via blockchain technology, offering anonymity and decentralization. It is ideal for businesses looking to accept cryptocurrency payments and expand their customer base to the digital currency market.', 
    89.99, 
    TRUE
    );

INSERT INTO public.supported_payment_methods (service_id, payment_method) 
VALUES
    (-1, 'Credit Card'),
    (-1, 'Debit Card'),
    (-2, 'QR Code'),
    (-3, 'PayPal'),
    (-4, 'Bitcoin');

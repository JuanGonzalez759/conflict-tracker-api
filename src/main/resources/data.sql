-- Insert Countries
INSERT INTO countries (id, name, code) VALUES (1, 'Ukraine', 'UKR');
INSERT INTO countries (id, name, code) VALUES (2, 'Russia', 'RUS');
INSERT INTO countries (id, name, code) VALUES (3, 'Israel', 'ISR');
INSERT INTO countries (id, name, code) VALUES (4, 'Palestine', 'PSE');
INSERT INTO countries (id, name, code) VALUES (5, 'United States', 'USA');
INSERT INTO countries (id, name, code) VALUES (6, 'Syria', 'SYR');
INSERT INTO countries (id, name, code) VALUES (7, 'Turkey', 'TUR');
INSERT INTO countries (id, name, code) VALUES (8, 'Yemen', 'YEM');
INSERT INTO countries (id, name, code) VALUES (9, 'Saudi Arabia', 'SAU');
INSERT INTO countries (id, name, code) VALUES (10, 'Iran', 'IRN');

-- Insert Conflicts
INSERT INTO conflicts (id, name, start_date, status, description) 
VALUES (1, 'Russia-Ukraine War', '2022-02-24', 'ACTIVE', 
'Full-scale military invasion of Ukraine by Russia that began on 24 February 2022. The conflict has resulted in significant civilian and military casualties, displacement of millions of Ukrainians, and widespread international condemnation.');

INSERT INTO conflicts (id, name, start_date, status, description) 
VALUES (2, 'Israeli-Palestinian Conflict', '1948-05-15', 'ACTIVE', 
'Long-running conflict between Israel and the Palestinians over land, sovereignty, and security. The conflict has its roots in the late 19th century and continues to this day with periodic escalations.');

INSERT INTO conflicts (id, name, start_date, status, description) 
VALUES (3, 'Syrian Civil War', '2011-03-15', 'PAUSED', 
'Ongoing multi-sided civil war in Syria between the Syrian government led by Bashar al-Assad and various opposition forces. The conflict has caused massive displacement and humanitarian crisis.');

INSERT INTO conflicts (id, name, start_date, status, description) 
VALUES (4, 'Yemeni Civil War', '2014-09-21', 'ENDED', 
'Ongoing conflict between the Yemeni government and the Houthi movement, with intervention from Saudi Arabia-led coalition. The war has led to one of the world''s worst humanitarian crises.');

-- Associate Countries with Conflicts (Many-to-Many)
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (1, 1); -- Ukraine
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (1, 2); -- Russia

INSERT INTO conflict_countries (conflict_id, country_id) VALUES (2, 3); -- Israel
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (2, 4); -- Palestine

INSERT INTO conflict_countries (conflict_id, country_id) VALUES (3, 6); -- Syria
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (3, 7); -- Turkey

INSERT INTO conflict_countries (conflict_id, country_id) VALUES (4, 8); -- Yemen
INSERT INTO conflict_countries (conflict_id, country_id) VALUES (4, 9); -- Saudi Arabia

-- Insert Factions
INSERT INTO factions (id, name, conflict_id) VALUES (1, 'Ukrainian Armed Forces', 1);
INSERT INTO factions (id, name, conflict_id) VALUES (2, 'Russian Armed Forces', 1);

INSERT INTO factions (id, name, conflict_id) VALUES (3, 'Israel Defense Forces', 2);
INSERT INTO factions (id, name, conflict_id) VALUES (4, 'Hamas', 2);

INSERT INTO factions (id, name, conflict_id) VALUES (5, 'Syrian Government Forces', 3);
INSERT INTO factions (id, name, conflict_id) VALUES (6, 'Syrian Opposition', 3);

INSERT INTO factions (id, name, conflict_id) VALUES (7, 'Yemeni Government', 4);
INSERT INTO factions (id, name, conflict_id) VALUES (8, 'Houthi Movement', 4);

-- Associate Factions with Supporting Countries (Many-to-Many)
INSERT INTO faction_countries (faction_id, country_id) VALUES (1, 1); -- Ukrainian Forces - Ukraine
INSERT INTO faction_countries (faction_id, country_id) VALUES (1, 5); -- Ukrainian Forces - USA support

INSERT INTO faction_countries (faction_id, country_id) VALUES (2, 2); -- Russian Forces - Russia

INSERT INTO faction_countries (faction_id, country_id) VALUES (3, 3); -- IDF - Israel
INSERT INTO faction_countries (faction_id, country_id) VALUES (3, 5); -- IDF - USA support

INSERT INTO faction_countries (faction_id, country_id) VALUES (4, 4); -- Hamas - Palestine
INSERT INTO faction_countries (faction_id, country_id) VALUES (4, 10); -- Hamas - Iran support

INSERT INTO faction_countries (faction_id, country_id) VALUES (5, 6); -- Syrian Gov - Syria
INSERT INTO faction_countries (faction_id, country_id) VALUES (5, 2); -- Syrian Gov - Russia support
INSERT INTO faction_countries (faction_id, country_id) VALUES (5, 10); -- Syrian Gov - Iran support

INSERT INTO faction_countries (faction_id, country_id) VALUES (6, 7); -- Syrian Opposition - Turkey support

INSERT INTO faction_countries (faction_id, country_id) VALUES (7, 8); -- Yemeni Gov - Yemen
INSERT INTO faction_countries (faction_id, country_id) VALUES (7, 9); -- Yemeni Gov - Saudi support

INSERT INTO faction_countries (faction_id, country_id) VALUES (8, 10); -- Houthis - Iran support

-- Insert Events
INSERT INTO events (id, event_date, location, description, conflict_id) 
VALUES (1, '2022-02-24', 'Kyiv, Ukraine', 'Russian invasion begins with strikes on multiple Ukrainian cities including the capital Kyiv.', 1);

INSERT INTO events (id, event_date, location, description, conflict_id) 
VALUES (2, '2022-04-02', 'Bucha, Ukraine', 'Ukrainian forces retake Bucha, revealing evidence of potential war crimes during Russian occupation.', 1);

INSERT INTO events (id, event_date, location, description, conflict_id) 
VALUES (3, '2023-06-06', 'Kakhovka, Ukraine', 'Kakhovka Dam is destroyed, causing massive flooding and environmental disaster in southern Ukraine.', 1);

INSERT INTO events (id, event_date, location, description, conflict_id) 
VALUES (4, '2023-10-07', 'Gaza Strip', 'Hamas launches surprise attack on southern Israel, triggering major military escalation.', 2);

INSERT INTO events (id, event_date, location, description, conflict_id) 
VALUES (5, '2021-05-10', 'Gaza Strip', 'Escalation of violence between Israel and Palestinian militant groups in Gaza.', 2);

INSERT INTO events (id, event_date, location, description, conflict_id) 
VALUES (6, '2013-08-21', 'Ghouta, Syria', 'Chemical weapons attack in Ghouta kills hundreds of civilians, crossing international red lines.', 3);

INSERT INTO events (id, event_date, location, description, conflict_id) 
VALUES (7, '2016-12-13', 'Aleppo, Syria', 'Syrian government forces recapture eastern Aleppo after years of rebel control.', 3);

INSERT INTO events (id, event_date, location, description, conflict_id) 
VALUES (8, '2015-03-26', 'Sanaa, Yemen', 'Saudi Arabia-led coalition begins military intervention in Yemen with airstrikes.', 4);

INSERT INTO events (id, event_date, location, description, conflict_id) 
VALUES (9, '2018-06-13', 'Hodeidah, Yemen', 'Coalition forces launch offensive on the strategic port city of Hodeidah.', 4);

-- Reset auto-increment counters for H2 Identity columns
-- Set the next value to be greater than the highest inserted ID
-- This ensures new records won't have ID conflicts
ALTER TABLE countries ALTER COLUMN id RESTART WITH 11;
ALTER TABLE conflicts ALTER COLUMN id RESTART WITH 5;
ALTER TABLE factions ALTER COLUMN id RESTART WITH 9;
ALTER TABLE events ALTER COLUMN id RESTART WITH 10;

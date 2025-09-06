#!/bin/bash
set -e

APP_DIR=~/bingeo/backend
PROPS_FILE="$APP_DIR/src/main/resources/application.properties"

echo "🔧 Updating Hibernate ddl-auto mode to 'update'..."
# Replace create-drop with update
sed -i 's/^spring.jpa.hibernate.ddl-auto=create-drop/spring.jpa.hibernate.ddl-auto=update/' "$PROPS_FILE"

echo "📦 Resetting Postgres schema..."
sudo -u postgres psql -d bingeo <<EOF
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL PRIVILEGES ON SCHEMA public TO bingeo_user;
EOF

echo "🎬 Inserting sample movies..."
sudo -u postgres psql -d bingeo <<'EOF'
INSERT INTO movie (title, genre, release_year, rating, description) VALUES
('Sita Raman', 'Romance', 2022, 8.5, 'Indian romantic drama'),
('Last Samurai', 'Action/Drama', 2003, 8.0, 'Samurai epic starring Tom Cruise'),
('Last Dance', 'Drama', 1996, 7.2, 'Courtroom and redemption story'),
('Dhindhora', 'Comedy/Drama', 2021, 7.5, 'Indian web series by BB Ki Vines'),
('Wild Wild Country', 'Documentary', 2018, 8.2, 'OSHO and Rajneeshpuram story'),
('Bird Box', 'Thriller', 2018, 6.6, 'Post-apocalyptic thriller starring Sandra Bullock'),
('Sapta Sagaradaache Ello', 'Romance/Drama', 2023, 8.8, 'Kannada romantic movie'),
('Saiyan Mori Re', 'Drama', 2019, 7.0, 'Regional movie'),
('Bang Baaja Baaraat', 'Comedy/Romance', 2015, 7.2, 'Indian wedding drama'),
('The Gentlemen', 'Crime/Comedy', 2019, 7.8, 'Guy Ritchie gangster movie'),

-- Brad Pitt movies
('Mr & Mrs Smith', 'Action/Comedy', 2005, 6.5, 'Brad Pitt & Angelina Jolie action comedy'),
('The Curious Case of Benjamin Button', 'Drama/Fantasy', 2008, 7.8, 'A man ages backwards'),
('Babylon', 'Drama', 2022, 7.2, 'Hollywood golden age chaos'),
('Seven Years in Tibet', 'Drama', 1997, 7.1, 'Brad Pitt in Tibet'),
('Meet Joe Black', 'Drama/Romance', 1998, 7.2, 'Brad Pitt as Death'),
('Inglourious Basterds', 'War/Drama', 2009, 8.3, 'Tarantino WWII movie'),
('Allied', 'Drama/Thriller', 2016, 7.1, 'WWII spy romance'),
('Bullet Train', 'Action/Comedy', 2022, 7.3, 'Assassins on a train'),
('Fight Club', 'Drama', 1999, 8.8, 'Cult classic'),

-- Leonardo DiCaprio movies
('The Departed', 'Crime/Thriller', 2006, 8.5, 'Scorsese crime thriller'),
('Inception', 'Sci-Fi/Thriller', 2010, 8.8, 'Dream heist movie'),
('Shutter Island', 'Thriller', 2010, 8.2, 'Psychological thriller'),
('Django Unchained', 'Western', 2012, 8.4, 'Tarantino western'),
('Blood Diamond', 'Drama/Thriller', 2006, 8.0, 'Conflict diamond drama'),
('The Revenant', 'Adventure/Drama', 2015, 8.0, 'Survival epic'),

-- Matt Damon
('Good Will Hunting', 'Drama', 1997, 8.3, 'Mathematical genius story'),
('The Martian', 'Sci-Fi', 2015, 8.0, 'Stranded on Mars'),

-- Christian Bale
('The Prestige', 'Mystery/Thriller', 2006, 8.5, 'Rival magicians'),
('American Hustle', 'Crime/Drama', 2013, 7.2, 'Con artist story'),
('The Batman Begins', 'Action', 2005, 8.2, 'Batman reboot'),
('The Dark Knight', 'Action', 2008, 9.0, 'Joker and Batman epic'),
('The Dark Knight Rises', 'Action', 2012, 8.4, 'Final Nolan Batman'),

-- Tom Hardy
('Venom', 'Action/Sci-Fi', 2018, 6.7, 'Marvel antihero'),
('Venom: Let There Be Carnage', 'Action/Sci-Fi', 2021, 6.0, 'Sequel to Venom'),
('Legend', 'Crime/Drama', 2015, 7.0, 'Kray twins story'),

-- Random/Oscar Winners
('Parasite', 'Drama/Thriller', 2019, 8.6, 'Oscar-winning Korean film'),
('Limitless', 'Sci-Fi/Thriller', 2011, 7.4, 'Drug-enhanced intelligence'),
('A Star is Born', 'Drama/Music', 2018, 7.6, 'Bradley Cooper & Lady Gaga'),
('Papillon', 'Drama', 2017, 7.2, 'Prison escape remake'),

-- Army movies
('Hacksaw Ridge', 'War/Drama', 2016, 8.1, 'True WWII story'),
('Rescue Dawn', 'War/Drama', 2006, 7.3, 'POW escape story'),
('Black Hawk Down', 'War/Action', 2001, 7.7, 'Somalia conflict'),
('Lone Survivor', 'War/Action', 2013, 7.5, 'Navy SEAL survival'),
('The Outpost', 'War/Action', 2019, 6.8, 'Battle in Afghanistan'),
('13 Hours', 'War/Action', 2016, 7.3, 'Benghazi US compound attack'),
('Zero Dark Thirty', 'War/Thriller', 2012, 7.4, 'Hunt for Bin Laden'),
('War Dogs', 'Comedy/Drama', 2016, 7.1, 'Arms dealers story'),
('The Covenant', 'War/Drama', 2023, 7.6, 'Guy Ritchie war drama'),
('Major', 'War/Drama', 2022, 8.2, 'Story of Maj. Unnikrishnan'),

-- Ben Affleck
('The Accountant', 'Thriller', 2016, 7.3, 'Math genius assassin'),
('Gone Girl', 'Thriller', 2014, 8.1, 'Psychological thriller'),
('Argo', 'Drama/Thriller', 2012, 7.7, 'Iran hostage crisis'),
('The Town', 'Crime/Thriller', 2010, 7.5, 'Bank heist drama'),
('Air', 'Drama', 2023, 7.5, 'Nike and Michael Jordan deal'),

-- Henry Cavill
('The Man from U.N.C.L.E.', 'Action/Comedy', 2015, 7.2, 'Spy comedy'),

-- More randoms
('Kingsman: The Golden Circle', 'Action/Comedy', 2017, 6.7, 'Kingsman 2'),
('Kingsman: The Secret Service', 'Action/Comedy', 2014, 7.7, 'Kingsman 1'),
('Into the Wild', 'Adventure/Drama', 2007, 8.1, 'True wilderness story'),
('Rush', 'Sports/Drama', 2013, 8.1, 'Formula 1 rivalry'),
('Everything Everywhere All at Once', 'Sci-Fi/Drama', 2022, 8.0, 'Multiverse chaos'),
('Nightcrawler', 'Thriller', 2014, 7.8, 'Crime journalism story'),
('Prisoners', 'Thriller', 2013, 8.1, 'Child abduction drama'),
('Tenet', 'Sci-Fi/Thriller', 2020, 7.3, 'Time inversion'),
('The Equalizer', 'Action/Thriller', 2014, 7.2, 'Denzel Washington vigilante'),
('The Shawshank Redemption', 'Drama', 1994, 9.3, 'Prison escape classic'),
('Inside Man', 'Crime/Thriller', 2006, 7.6, 'Bank heist thriller'),
('Safe House', 'Thriller', 2012, 6.7, 'CIA agent story'),
('Man on Fire', 'Thriller', 2004, 7.7, 'Revenge thriller'),
('The Boy in the Striped Pyjamas', 'Drama', 2008, 7.8, 'Holocaust story'),
('Dune', 'Sci-Fi', 2021, 8.0, 'Epic sci-fi Part 1'),
('Dune Part Two', 'Sci-Fi', 2024, 8.6, 'Epic sci-fi Part 2'),
('Uncharted', 'Adventure/Action', 2022, 6.5, 'Video game adaptation'),

-- Robert Downey Jr
('Zodiac', 'Thriller', 2007, 7.7, 'Zodiac killer investigation'),
('Sherlock Holmes', 'Mystery/Action', 2009, 7.6, 'Detective action'),
('Sherlock Holmes: A Game of Shadows', 'Mystery/Action', 2011, 7.5, 'Sequel to Sherlock Holmes'),

-- Daniel Craig
('Knives Out', 'Mystery/Drama', 2019, 7.9, 'Murder mystery'),
('Glass Onion', 'Mystery/Drama', 2022, 7.2, 'Sequel to Knives Out'),
('Layer Cake', 'Crime/Drama', 2004, 7.3, 'British crime drama');
EOF

echo "🚀 Starting backend..."
cd "$APP_DIR"
mvn spring-boot:run


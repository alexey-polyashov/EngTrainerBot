--MY DICTIONARY

--WORDS
INSERT INTO words (id, foreign_write, transcription, native_write, description)
VALUES (100, 'study', ' [ˈstʌdɪ]', 'изучение, исследование, изучать', 'their study - их исследование, if you study - если вы изучаете, study periods - периоды обучени'),
 (101,'youth', ' [ju:θ]', 'молодежь, молодежный, юность', 'wild youth - бурная молодость, with youth -  с молодежью, in my youth - в моей молодости'),
 (102, 'laugh', '[lɑ:f]', 'смех, смеятся, улыбаться, веселить', 'i laugh- я смеюсь, make people laugh - рассмешить'),
 (103, 'smile', '[smaɪl]', 'улыбка, улыбаться', 'a warm smile -теплая улыбка, fake smile - фальшивая улыбка, without a smile - без улыбки'),
 (104, 'pleasant', '[ˈpleznt]', 'приятный, милый, удовольствие', 'pleasant smell - приятный аромат, is not so pleasant - не такой приятный, in pleasant environments - в приятной остановке'),
 (105, 'slow down', '', 'замедлять, снизить скрость, медленнее, затормозить', 'we must slow down - мы должны замедлить, slow down please - помедленнее пожалуйста, slow down the business - замедлить развитие бизнеса'),
 (106, 'speak up', '', 'высказаться, выступать, громче', 'We must speak up - мы должны говрить, speak up about - говорить о'),
 (107, 'complete', '[kəmˈpli:t]', 'полный, совершенный, законченный, целый', 'most complete - наиболее полный, complete control - полный контроль, complete software product - завершенный программный прдукт'),
 (108, 'native', '[ˈneɪtɪv]', 'родной, коренной, местный', 'native language - родной язык, native forests - местные леса, native people- коренной народ, i am native - я местный');

--WORDS_DICTIONARIES
INSERT INTO words_dictionaries (words_id, dictionaries_id)
VALUES (100, 1),
 (101, 1),
 (102, 1),
 (103, 1),
 (104, 1),
 (105, 1),
 (106, 1),
 (107, 1),

 (108, 1);

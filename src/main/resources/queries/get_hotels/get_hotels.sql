select id,  -- not null
    name,   -- not null
    address -- not null
from hotels
order by id
limit :limit
offset :offset
